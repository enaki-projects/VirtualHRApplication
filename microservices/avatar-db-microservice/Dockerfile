# syntax=docker/dockerfile:1
# Multi-stage, multi-arch (linux/arm64 + linux/amd64). Builds from source.
# The jar is platform-independent, so the Maven stage runs on the build host's platform.
FROM --platform=$BUILDPLATFORM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY .mvn/ .mvn/
# Resolve dependencies first for better layer caching.
RUN mvn -B -q -Dmaven.test.skip=true dependency:go-offline || true
COPY src/ src/
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
# Non-root runtime user.
RUN groupadd --system app && useradd --system --gid app --home /app app

# The avatar resources (avatar.xml, available_languages/, avatar/ with 1.3 GB of videos)
# are NOT baked into the image. Compose mounts src/main/resources read-only at
# /app/resources, so a rebuild never re-copies the videos.
COPY --from=build /build/target/avatar_db-1.0.0.jar /app/AvatarDbMicroservice.jar
RUN mkdir -p /app/resources && chown -R app:app /app
USER app

ENV AVATAR_RESOURCES_DIR=/app/resources \
    SERVER_PORT=2010
EXPOSE 2010
ENTRYPOINT ["java","-jar","/app/AvatarDbMicroservice.jar"]
