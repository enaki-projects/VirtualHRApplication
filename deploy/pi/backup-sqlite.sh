#!/usr/bin/env bash
# Nightly backup of the Virtual HR data on the Pi. Installed by deploy-to-pi.sh as
# /etc/cron.d/virtualhr-backup (03:30 every night, as root, log in /var/log/virtualhr-backup.log).
#
# Writes /srv/virtualhr/backups/YYYY-MM-DD/ with:
#   cv.db.gz, manager.db.gz   consistent copies made with SQLite's online backup API,
#                             safe while the services are writing
#   cv-images.tar.gz          user photos
# and deletes backup folders older than KEEP_DAYS (default 14).
#
# Run by hand:   sudo bash /srv/virtualhr/deploy/pi/backup-sqlite.sh
# Restore one:   cd /srv/virtualhr && docker compose stop cv-db
#                gunzip -c backups/<date>/cv.db.gz | sudo tee data/cv-db/cv.db >/dev/null
#                sudo chown 999:999 data/cv-db/cv.db && docker compose start cv-db
set -euo pipefail

ROOT="${ROOT:-/srv/virtualhr}"
DATA_DIR="$ROOT/data"
OUT="$ROOT/backups"
KEEP_DAYS="${KEEP_DAYS:-14}"

[[ $EUID -eq 0 ]] || { echo "Run with sudo" >&2; exit 1; }
umask 077
dest="$OUT/$(date +%F)"
mkdir -p "$dest"

python3 - "$DATA_DIR" "$dest" <<'PY'
import gzip, shutil, sqlite3, sys
from pathlib import Path

src, dst = Path(sys.argv[1]), Path(sys.argv[2])
for rel in ("cv-db/cv.db", "manager-db/manager.db"):
    db = src / rel
    if not db.exists():
        print(f"skip {db}: not found")
        continue
    tmp = dst / (db.name + ".tmp")
    # Read-only URI: never creates journal files owned by root next to the live database.
    with sqlite3.connect(f"file:{db}?mode=ro", uri=True) as live, sqlite3.connect(tmp) as copy:
        live.backup(copy)
        ok = copy.execute("PRAGMA integrity_check").fetchone()[0]
    if ok != "ok":
        raise SystemExit(f"{db}: integrity_check returned {ok!r}")
    with open(tmp, "rb") as f, gzip.open(dst / (db.name + ".gz"), "wb") as g:
        shutil.copyfileobj(f, g)
    tmp.unlink()
    print(f"{db} -> {dst / (db.name + '.gz')}")
PY

if [[ -d "$DATA_DIR/cv-db/images" ]]; then
  tar -czf "$dest/cv-images.tar.gz" -C "$DATA_DIR/cv-db" images
fi

find "$OUT" -mindepth 1 -maxdepth 1 -type d -name '20??-??-??' -mtime +"$KEEP_DAYS" -print -exec rm -r -- {} +
echo "$(date -Is) backup done: $dest ($(du -sh "$dest" | cut -f1))"
