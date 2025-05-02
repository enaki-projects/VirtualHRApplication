FROM openjdk:8-jdk-alpine
RUN mkdir -p /app/src/main/resources
ADD target/jwt_provider-1.0.0.jar app/jwt_provider-1.0.0.jar

WORKDIR /app/src/main/resources

COPY ./src/main/resources/. .
WORKDIR ../../../

EXPOSE 2012

ENTRYPOINT ["java","-jar", "jwt_provider-1.0.0.jar"]