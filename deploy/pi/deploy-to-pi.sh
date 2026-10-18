#!/usr/bin/env bash
# Deploy Virtual HR from this Mac to the Raspberry Pi.
#
#   ./deploy/pi/deploy-to-pi.sh                 # build, copy, start, smoke-test
#   ./deploy/pi/deploy-to-pi.sh --skip-build    # reuse the images already built locally
#   ./deploy/pi/deploy-to-pi.sh --skip-videos   # don't re-sync the 1.3 GB avatar videos
#   ./deploy/pi/deploy-to-pi.sh --smoke-only    # only wait for the backends and re-run the smoke test
#
# Needs on the Mac: docker (Colima) with compose, rsync, ssh.
# Needs on the Pi:  run deploy/pi/pi-setup.sh once first (installs Docker + nginx on port 8080).
#
# PUBLIC_URL defaults to http://virtual-hr-app.discutie.link:8080/ (used in recovery emails).
#
# Images are built on the Mac (Apple Silicon = arm64, same as the Pi) and streamed
# over SSH with `docker save | docker load`, so the Pi never compiles anything.
set -euo pipefail

PUBLIC_URL="${PUBLIC_URL:-http://virtual-hr-app.discutie.link:8080/}"
PI="${PI:-enaki@192.168.1.48}"
REMOTE_DIR="${REMOTE_DIR:-/srv/virtualhr}"
PLATFORM="${PLATFORM:-linux/arm64}"
SKIP_BUILD=0
SKIP_VIDEOS=0
SMOKE_ONLY=0
for arg in "$@"; do
  case "$arg" in
    --skip-build) SKIP_BUILD=1 ;;
    --skip-videos) SKIP_VIDEOS=1 ;;
    --smoke-only) SMOKE_ONLY=1 ;;
    *) echo "unknown option: $arg" >&2; exit 2 ;;
  esac
done

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
cd "$ROOT"

step() { printf '\n\033[1;34m==> %s\033[0m\n' "$*"; }

[[ -f .env ]] || { echo "Missing $ROOT/.env (copy .env.example and fill in JWT_SECRET and SESSION_SECRET)" >&2; exit 1; }
grep -qE '^JWT_SECRET=.{32,}' .env || { echo "JWT_SECRET in .env must be at least 32 characters" >&2; exit 1; }
grep -qE '^SESSION_SECRET=.+' .env || { echo "SESSION_SECRET in .env is empty" >&2; exit 1; }

# One SSH connection for the whole run, so the password is asked once.
SOCK="$(mktemp -u "${TMPDIR:-/tmp}/vhr-ssh.XXXXXX")"
SSH_OPTS=(-o ControlMaster=auto -o ControlPath="$SOCK" -o ControlPersist=600)
cleanup() { ssh "${SSH_OPTS[@]}" -O exit "$PI" 2>/dev/null || true; }
trap cleanup EXIT
pi() { ssh "${SSH_OPTS[@]}" "$PI" "$@"; }

step "Connecting to $PI"
pi 'echo "connected to $(hostname) ($(uname -m))"; command -v docker >/dev/null || { echo "Docker is not installed on the Pi: run deploy/pi/pi-setup.sh there first"; exit 1; }'
pi "sudo mkdir -p '$REMOTE_DIR' && sudo chown \"\$(id -un)\": '$REMOTE_DIR'"

if [[ $SMOKE_ONLY -eq 0 ]]; then
IMAGES=$(IMAGE_PREFIX=virtualhr docker compose config --images | sort -u)

if [[ $SKIP_BUILD -eq 0 ]]; then
  step "Building images for $PLATFORM"
  DOCKER_DEFAULT_PLATFORM="$PLATFORM" docker compose build
fi

step "Copying images to the Pi (only layers the Pi does not have yet are sent)"
for img in $IMAGES; do
  local_id=$(docker image inspect --format '{{.Id}}' "$img")
  remote_id=$(pi "docker image inspect --format '{{.Id}}' '$img' 2>/dev/null" || true)
  if [[ "$local_id" == "$remote_id" ]]; then
    echo "  $img: already up to date"
  else
    echo "  $img: sending $(docker image inspect --format '{{.Size}}' "$img" | awk '{printf "%.0f MB", $1/1048576}') uncompressed"
    docker save "$img" | gzip -1 | pi 'gunzip | docker load -q'
  fi
done

step "Syncing compose files to $REMOTE_DIR"
rsync -az -e "ssh ${SSH_OPTS[*]}" docker-compose.yml docker-compose.pi.yml .env.example deploy "$PI:$REMOTE_DIR/"
# Secrets: written over ssh with umask 077 so the file is never readable by others.
# (macOS ships openrsync, whose --chmod and --info differ from GNU rsync.)
pi "umask 077 && cat > '$REMOTE_DIR/.env' && chmod 600 '$REMOTE_DIR/.env'" < .env
pi "sed -i 's#^PUBLIC_URL=.*#PUBLIC_URL=${PUBLIC_URL}#' '$REMOTE_DIR/.env'; grep -q '^PUBLIC_URL=' '$REMOTE_DIR/.env' || echo 'PUBLIC_URL=${PUBLIC_URL}' >> '$REMOTE_DIR/.env'"
# On the Pi every `docker compose` command (including ones you type by hand) also loads
# docker-compose.pi.yml, which keeps the SQLite data in /srv/virtualhr/data.
pi "sed -i '/^COMPOSE_FILE=/d' '$REMOTE_DIR/.env' && echo 'COMPOSE_FILE=docker-compose.yml:docker-compose.pi.yml' >> '$REMOTE_DIR/.env'"

RES=microservices/avatar-db-microservice/src/main/resources
if [[ $SKIP_VIDEOS -eq 0 ]]; then
  step "Syncing avatar scenarios and videos (1.3 GB the first time, then only changes)"
  pi "mkdir -p '$REMOTE_DIR/$RES'"
  rsync -a --progress --delete --exclude keystore/ -e "ssh ${SSH_OPTS[*]}" "$RES/" "$PI:$REMOTE_DIR/$RES/"
fi

step "Preparing the data folders in $REMOTE_DIR/data (moves old volume data once)"
pi "cd '$REMOTE_DIR' && sudo DATA_DIR='$REMOTE_DIR/data' bash deploy/pi/prepare-data.sh"

step "Starting the stack on the Pi"
pi "cd '$REMOTE_DIR' && docker compose up -d --no-build --pull never --remove-orphans && docker image prune -f >/dev/null"

step "Installing the nightly SQLite backup (03:30, keeps 14 days in $REMOTE_DIR/backups)"
pi "echo '30 3 * * * root ROOT=$REMOTE_DIR /bin/bash $REMOTE_DIR/deploy/pi/backup-sqlite.sh >> /var/log/virtualhr-backup.log 2>&1' | sudo tee /etc/cron.d/virtualhr-backup >/dev/null"
fi

step "Waiting for the backends (the JVMs take 30-90 s to start on a Pi 4)"
# /healthz and the UIs are served by nginx itself, so they are up instantly. Wait until
# every proxied backend answers with anything other than 502/503/504.
ready=0
for i in $(seq 1 48); do
  if pi 'for u in /admin/login /api/geography/countries "/avatar-api/avatar-info?name=candidate_intro_1&category=candidate&subcategory=intro&resolution=720x720&language=en"; do
           c=$(curl -s -o /dev/null -w "%{http_code}" "http://127.0.0.1:8088$u"); case "$c" in 000|502|503|504) exit 1;; esac
         done' 2>/dev/null; then ready=1; break; fi
  printf '  waiting (%ss)\r' "$((i * 5))"; sleep 5
done
[[ $ready -eq 1 ]] || echo "Backends still not answering after 4 minutes; running the smoke test anyway."

step "Smoke test on the Pi"
if pi "cd '$REMOTE_DIR' && python3 deploy/smoke_test.py http://127.0.0.1:8088"; then
  printf '\n\033[1;32mDeployed.\033[0m Open %s (test from mobile data if your router has no NAT loopback).\n' "$PUBLIC_URL"
else
  echo; echo "Smoke test failed. Logs: ssh $PI 'cd $REMOTE_DIR && docker compose logs --tail 50'" >&2
  exit 1
fi
