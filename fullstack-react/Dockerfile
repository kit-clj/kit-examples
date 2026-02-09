# syntax = docker/dockerfile:1.2
FROM node:20-slim AS client-build

WORKDIR /client
COPY client/package.json client/package-lock.json ./
RUN npm ci
COPY client/ ./
RUN npm run build

FROM clojure:openjdk-11 AS build

WORKDIR /app
COPY . /app
COPY --from=client-build /client/dist/ /app/resources/public/
RUN clj -Sforce -T:build all

FROM adoptopenjdk/openjdk11:alpine-slim

COPY --from=build /app/target/fullstack-react-standalone.jar /app/fullstack-react.jar

EXPOSE $PORT

ENTRYPOINT exec java $JAVA_OPTS -jar /app/fullstack-react.jar
