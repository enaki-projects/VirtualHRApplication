# First Method
#FROM node:alpine
#
#RUN mkdir -p /app
#WORKDIR /app
#
#COPY package.json /app
#COPY package-lock.json /app
#
#RUN npm install
#
#COPY . /app
#EXPOSE 3000
#
#CMD node_modules/.bin/ng serve --host 0.0.0.0 --disableHostCheck

# Second method
FROM node:alpine
RUN mkdir -p /app
WORKDIR /app

ENV PORT 3000

COPY package.json /app
COPY package-lock.json /app
COPY . /app

RUN npm install

EXPOSE 3000
CMD node server.js
