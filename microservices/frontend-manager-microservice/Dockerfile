FROM node:alpine

RUN mkdir -p /app
WORKDIR /app

ENV PORT 3000
ENV NODE_ENV 'production'

COPY package.json /app
COPY package-lock.json /app
COPY . /app

EXPOSE 3000
CMD node server.js
