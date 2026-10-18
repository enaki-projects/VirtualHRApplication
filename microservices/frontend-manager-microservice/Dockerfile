# syntax=docker/dockerfile:1
# frontend-manager: React 19 + Vite, served as static files under /manager/.
# The JS bundle is platform-independent, so the build stage runs natively.
FROM --platform=$BUILDPLATFORM node:24-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY . .
RUN npm run build

FROM nginxinc/nginx-unprivileged:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html/manager
EXPOSE 8080
