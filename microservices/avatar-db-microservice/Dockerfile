FROM openjdk:8-jdk-alpine
RUN mkdir -p /app/src/main/resources
ADD target/avatar_db-1.0.0.jar app/AvatarDbMicroservice.jar

WORKDIR /app/src/main/resources

COPY ./src/main/resources/. .
WORKDIR ../../../

EXPOSE 2010

ENTRYPOINT ["java","-jar", "AvatarDbMicroservice.jar"]