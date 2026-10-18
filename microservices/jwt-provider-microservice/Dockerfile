# syntax=docker/dockerfile:1
# Multi-stage, multi-arch (linux/arm64 + linux/amd64). Builds from source (incl. JAXB codegen).
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY .mvn/ .mvn/
RUN mvn -B -q -Dmaven.test.skip=true dependency:go-offline || true
COPY src/ src/
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --home /app app
COPY --from=build /build/target/jwt_provider-1.0.0.jar /app/jwt_provider-1.0.0.jar
RUN chown -R app:app /app
USER app

# JWT_SECRET and CV_DB_URL are supplied at runtime (compose/.env). No secret is baked in.
ENV SERVER_PORT=2012 \
    CV_DB_URL=http://cv-db:2011
EXPOSE 2012
ENTRYPOINT ["java","-jar","/app/jwt_provider-1.0.0.jar"]
