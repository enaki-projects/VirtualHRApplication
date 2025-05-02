#FROM openjdk:8-jdk-alpine
FROM adoptopenjdk/openjdk8:alpine-slim
RUN mkdir -p /app/src/main/resources
ADD target/cv_db-1.0.0.jar app/CVDbMicroservice.jar

COPY cv.db app/.

WORKDIR /app/src/main/resources

COPY ./src/main/resources/. .
WORKDIR ../../../

EXPOSE 2011
ENTRYPOINT ["java","-jar", "CVDbMicroservice.jar"]