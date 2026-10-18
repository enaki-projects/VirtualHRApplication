# syntax=docker/dockerfile:1
# frontend-admin: Express 5 + EJS, server-side calls to cv-adapter. Multi-arch.
FROM node:24-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci --no-audit --no-fund
COPY tsconfig.json ./
COPY src ./src
RUN npm run build

FROM node:24-alpine AS runtime
WORKDIR /app
ENV NODE_ENV=production \
    PORT=3000
COPY package.json package-lock.json ./
RUN npm ci --omit=dev --no-audit --no-fund && npm cache clean --force
COPY --from=build /app/dist ./dist
COPY views ./views
COPY public ./public
# SESSION_SECRET, ADAPTER_URL and BASE_PATH come from compose / .env.
USER node
EXPOSE 3000
CMD ["node", "dist/app.js"]
