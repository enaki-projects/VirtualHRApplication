#!/usr/bin/env bash
# Deploy Virtual HR from this Mac to the Raspberry Pi.
#
#   ./deploy/pi/deploy-to-pi.sh                 # build, copy, start, smoke-test
#   ./deploy/pi/deploy-to-pi.sh --skip-build    # reuse the images already built locally
#   ./deploy/pi/deploy-to-pi.sh --skip-videos   # don't re-sync the 1.3 GB avatar videos
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
for arg in "$@"; do
  case "$arg" in
    --skip-build) SKIP_BUILD=1 ;;
    --skip-videos) SKIP_VIDEOS=1 ;;
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
rsync -az -e "ssh ${SSH_OPTS[*]}" docker-compose.yml .env.example deploy "$PI:$REMOTE_DIR/"
rsync -az --chmod=F600 -e "ssh ${SSH_OPTS[*]}" .env "$PI:$REMOTE_DIR/.env"
pi "sed -i 's#^PUBLIC_URL=.*#PUBLIC_URL=${PUBLIC_URL}#' '$REMOTE_DIR/.env'; grep -q '^PUBLIC_URL=' '$REMOTE_DIR/.env' || echo 'PUBLIC_URL=${PUBLIC_URL}' >> '$REMOTE_DIR/.env'"

RES=microservices/avatar-db-microservice/src/main/resources
if [[ $SKIP_VIDEOS -eq 0 ]]; then
  step "Syncing avatar scenarios and videos (1.3 GB the first time, then only changes)"
  pi "mkdir -p '$REMOTE_DIR/$RES'"
  rsync -a --info=progress2 --delete --exclude keystore/ -e "ssh ${SSH_OPTS[*]}" "$RES/" "$PI:$REMOTE_DIR/$RES/"
fi

step "Starting the stack on the Pi"
pi "cd '$REMOTE_DIR' && docker compose up -d --no-build --pull never --remove-orphans && docker image prune -f >/dev/null"

step "Waiting for the services (the JVMs take 30-60 s to start on a Pi 4)"
for _ in $(seq 1 36); do
  if pi "curl -fs -o /dev/null http://127.0.0.1:8088/healthz && curl -fs -o /dev/null http://127.0.0.1:8088/manager/" 2>/dev/null; then break; fi
  sleep 5
done

step "Smoke test on the Pi"
if pi "cd '$REMOTE_DIR' && python3 deploy/smoke_test.py http://127.0.0.1:8088"; then
  printf '\n\033[1;32mDeployed.\033[0m Open %s (test from mobile data if your router has no NAT loopback).\n' "$PUBLIC_URL"
else
  echo; echo "Smoke test failed. Logs: ssh $PI 'cd $REMOTE_DIR && docker compose logs --tail 50'" >&2
  exit 1
fi
