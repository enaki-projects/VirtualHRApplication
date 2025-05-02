FROM adoptopenjdk/openjdk8:alpine-slim
RUN mkdir -p /app/src/main/resources
ADD target/manager_db-1.0.0.jar app/ManagerDbMicroservice.jar

COPY manager.db app/.

WORKDIR /app/src/main/resources

COPY ./src/main/resources/. .
WORKDIR ../../../

EXPOSE 2015

ENTRYPOINT ["java","-jar", "ManagerDbMicroservice.jar"]