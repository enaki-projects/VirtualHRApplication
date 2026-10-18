# syntax=docker/dockerfile:1
# Multi-stage, multi-arch (linux/arm64 + linux/amd64). Builds from source.
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

# Ship the repo's SQLite database as a read-only seed. On first start the app copies it to
# DB_PATH (/data/manager.db) if that file does not already exist; existing data is preserved.
COPY manager.db /app/seed/manager.db

COPY --from=build /build/target/manager_db-1.0.0.jar /app/ManagerDbMicroservice.jar
RUN mkdir -p /data && chown -R app:app /app /data
USER app

VOLUME ["/data"]
ENV DB_PATH=/data/manager.db \
    SEED_DB_PATH=/app/seed/manager.db \
    SERVER_PORT=2015
EXPOSE 2015
ENTRYPOINT ["java","-jar","/app/ManagerDbMicroservice.jar"]
