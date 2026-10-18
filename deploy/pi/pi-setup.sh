#!/usr/bin/env bash
# One-time Raspberry Pi setup for Virtual HR, next to the existing Graf-Quest site.
#
# On the Pi:   sudo bash pi-setup.sh             # public port 8080 (default)
#              sudo PUBLIC_PORT=9090 bash pi-setup.sh
#
# Graf-Quest is NOT touched: its systemd service, port 3000 and the Livebox rule
# WAN 80 -> 192.168.1.48:3000 all stay exactly as they are.
#
# What it changes:
#   * installs Docker Engine + compose plugin (official Docker apt repo) and nginx
#   * nginx listens ONLY on $PUBLIC_PORT (default 8080) and forwards to the compose
#     gateway on 127.0.0.1:8088. nginx's stock port-80 site is removed, so nginx never
#     binds port 80. The compose stack itself publishes nothing beyond 127.0.0.1.
#   * pauses while you add a NEW Livebox rule WAN $PUBLIC_PORT -> 192.168.1.48:$PUBLIC_PORT
#
# Result: http://virtual-hr-app.discutie.link:$PUBLIC_PORT/
# (DNS cannot carry a port, so the port is part of the URL. The CloudFront phase later
#  serves the site on plain https://virtual-hr-app.discutie.link/ and fetches from this port.)
#
# Undo: sudo rm /etc/nginx/sites-enabled/virtualhr.conf && sudo systemctl stop nginx,
#       and delete the Livebox rule for $PUBLIC_PORT.
set -euo pipefail

PUBLIC_PORT="${PUBLIC_PORT:-8080}"
VHR_HOST=virtual-hr-app.discutie.link
APP_USER="${SUDO_USER:-$(id -un)}"

step() { printf '\n\033[1;34m==> %s\033[0m\n' "$*"; }
[[ $EUID -eq 0 ]] || { echo "Run with sudo: sudo bash $0" >&2; exit 1; }
[[ "$(uname -m)" == "aarch64" ]] || { echo "Needs 64-bit Raspberry Pi OS (uname -m = aarch64); this is $(uname -m)" >&2; exit 1; }
[[ "$PUBLIC_PORT" =~ ^[0-9]+$ && "$PUBLIC_PORT" -ge 1024 && "$PUBLIC_PORT" -le 65535 ]] \
  || { echo "PUBLIC_PORT must be 1024-65535 (CloudFront can only use those as an origin port)" >&2; exit 1; }
for p in 3000 8088; do
  [[ "$PUBLIC_PORT" != "$p" ]] || { echo "PUBLIC_PORT $p is already used (3000 = Graf-Quest, 8088 = compose gateway)" >&2; exit 1; }
done
if ss -ltnH "sport = :$PUBLIC_PORT" | grep -q . && ! ss -ltnpH "sport = :$PUBLIC_PORT" | grep -q nginx; then
  echo "Port $PUBLIC_PORT is already in use on the Pi:" >&2; ss -ltnp "sport = :$PUBLIC_PORT" >&2; exit 1
fi

step "Root filesystem (Docker images and the SQLite volumes live here)"
findmnt -no SOURCE,SIZE,AVAIL / || df -h /

if ! command -v docker >/dev/null || ! docker compose version >/dev/null 2>&1; then
  step "Installing Docker Engine + compose plugin from download.docker.com"
  apt-get update -qq
  apt-get install -y -qq ca-certificates curl
  install -m 0755 -d /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
  chmod a+r /etc/apt/keyrings/docker.asc
  codename="$(. /etc/os-release && echo "$VERSION_CODENAME")"
  echo "deb [arch=arm64 signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian $codename stable" \
    > /etc/apt/sources.list.d/docker.list
  apt-get update -qq
  apt-get install -y -qq docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
  systemctl enable --now docker
fi
usermod -aG docker "$APP_USER"
docker --version; docker compose version

step "Installing nginx (port $PUBLIC_PORT only)"
apt-get install -y -qq nginx rsync python3
# The Debian package ships a port-80 default site; drop it so nginx never binds 80.
rm -f /etc/nginx/sites-enabled/default

cat > /etc/nginx/sites-available/virtualhr.conf <<EOF
# Managed by pi-setup.sh (VirtualHRApplication).
# Public entry for Virtual HR on port $PUBLIC_PORT -> compose gateway on 127.0.0.1:8088.
# Graf-Quest is not proxied here; it keeps serving port 3000 directly.
server {
    listen $PUBLIC_PORT default_server;
    listen [::]:$PUBLIC_PORT default_server;
    server_name $VHR_HOST _;

    client_max_body_size 20m;
    server_tokens off;

    location / {
        proxy_pass http://127.0.0.1:8088;
        proxy_http_version 1.1;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
        proxy_set_header Connection "";
        proxy_read_timeout 120s;
        # Avatar videos: stream with Range support instead of buffering to disk.
        proxy_buffering off;
        proxy_set_header Range \$http_range;
        proxy_set_header If-Range \$http_if_range;
    }
}
EOF
ln -sfn /etc/nginx/sites-available/virtualhr.conf /etc/nginx/sites-enabled/virtualhr.conf
nginx -t
systemctl enable nginx >/dev/null 2>&1
systemctl restart nginx

step "Listening sockets (expect nginx on :$PUBLIC_PORT, nothing from nginx on :80)"
ss -ltnp | grep -E "nginx|:3000 |:8088 " || true
if ss -ltnpH "sport = :80" | grep -q nginx; then
  echo "nginx is listening on port 80; check /etc/nginx/sites-enabled/ for another site" >&2; exit 1
fi
systemctl is-active grafquest 2>/dev/null && echo "grafquest.service still active, untouched" || true

step "Local check through nginx"
curl -s -o /dev/null -w "virtual-hr-app via nginx: %{http_code} (502 is expected until the stack is deployed)\n" \
  "http://127.0.0.1:$PUBLIC_PORT/" || true

cat <<EOF

$(printf '\033[1;33m')ACTION ON THE LIVEBOX$(printf '\033[0m') (Advanced settings -> Network -> NAT/PAT)
  ADD a new rule (leave the existing WAN 80 -> 3000 rule for Graf-Quest alone):
    Application: VirtualHR   Protocol: TCP
    External port: $PUBLIC_PORT   Internal port: $PUBLIC_PORT   Device: 192.168.1.48

EOF
read -rp "Press Enter once the Livebox rule is saved... " _

PUBLIC_IP="$(curl -4fsS --max-time 10 https://checkip.amazonaws.com | tr -d '[:space:]' || true)"
echo "Public IP: ${PUBLIC_IP:-unknown}"
echo "Note: many routers do not loop back to their own public IP, so test from a phone on mobile data:"
echo "  http://$VHR_HOST:$PUBLIC_PORT/"

printf '\n\033[1;32mPi setup done.\033[0m Log out and back in (docker group), then on the Mac run:\n'
printf '  PUBLIC_URL=http://%s:%s/ ./deploy/pi/deploy-to-pi.sh\n' "$VHR_HOST" "$PUBLIC_PORT"
