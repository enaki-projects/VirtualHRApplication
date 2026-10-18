# syntax=docker/dockerfile:1

# ---- Build stage: compile the Angular app from source ----
FROM node:24-alpine AS build
WORKDIR /app

# Install dependencies first (better layer caching).
# ngx-toastr@20 declares an Angular 21 peer range but is runtime-compatible with
# Angular 22; --legacy-peer-deps lets the (otherwise green) install resolve.
COPY package.json package-lock.json* ./
RUN npm ci --legacy-peer-deps || npm install --legacy-peer-deps

COPY . .
# Produces dist/frontend-user-microservice/ (browser output, base href '/').
RUN npm run build -- --configuration production

# ---- Runtime stage: static nginx serving the SPA ----
FROM nginx:1.27-alpine AS runtime

# Master runs as root only to bind port 80; workers drop to the unprivileged
# 'nginx' user automatically. No secrets are baked into the image.
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/frontend-user-microservice/ /usr/share/nginx/html/

EXPOSE 80

HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD wget -q -O /dev/null http://127.0.0.1:80/ || exit 1
