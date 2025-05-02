FROM node:12

RUN mkdir -p /app/src
WORKDIR /app

COPY package*.json ./
COPY . .

RUN npm install

ENV AVATAR_IP=172.18.0.10
ENV ADAPTER_IP=172.18.0.50
ENV NODE_ENV='production'
ENV PORT=3000
ENV NODE_TLS_REJECT_UNAUTHORIZED='0'

EXPOSE 3000

CMD [ "node", "src/app.js" ]
