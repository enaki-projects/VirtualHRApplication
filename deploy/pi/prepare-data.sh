#!/usr/bin/env bash
# Prepare the host data folders used by docker-compose.pi.yml. Runs on the Pi, as root,
# from deploy-to-pi.sh before `docker compose up`. Safe to run any number of times.
#
# For each of cv-db and manager-db:
#   * creates /srv/virtualhr/data/<service>, owned by the uid the container runs as
#   * if that folder is empty and the old named volume virtualhr_<service>-data exists,
#     stops the service and copies the volume's contents in (the volume itself is kept;
#     remove it later with `docker volume rm virtualhr_<service>-data`)
#   * an empty folder with no old volume is left empty: the service seeds it on first start
set -euo pipefail

DATA_DIR="${DATA_DIR:-/srv/virtualhr/data}"
PROJECT="${COMPOSE_PROJECT_NAME:-virtualhr}"
IMAGE_PREFIX="${IMAGE_PREFIX:-virtualhr}"
TAG="${TAG:-latest}"

[[ $EUID -eq 0 ]] || { echo "Run with sudo" >&2; exit 1; }
cd "$(dirname "$0")/../.."

mkdir -p "$DATA_DIR"
chmod 755 "$DATA_DIR"

for svc in cv-db manager-db; do
  dir="$DATA_DIR/$svc"
  vol="${PROJECT}_${svc}-data"
  img="$IMAGE_PREFIX/$svc:$TAG"
  uid="$(docker run --rm --entrypoint id "$img" -u)"
  gid="$(docker run --rm --entrypoint id "$img" -g)"

  mkdir -p "$dir"
  if [[ -z "$(ls -A "$dir")" ]] && docker volume inspect "$vol" >/dev/null 2>&1; then
    echo "$svc: moving data from volume $vol to $dir"
    docker compose stop "$svc" >/dev/null 2>&1 || true
    docker run --rm --user 0 --entrypoint cp \
      -v "$vol":/from:ro -v "$dir":/to "$img" -a /from/. /to/
  fi
  chown -R "$uid:$gid" "$dir"
  chmod 750 "$dir"
  echo "$svc: $dir ($(du -sh "$dir" | cut -f1), owner $uid:$gid)"
done
