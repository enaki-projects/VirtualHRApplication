# syntax=docker/dockerfile:1

# ---- Build stage: compile + package from source (no prebuilt jar needed) ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Leverage layer caching: resolve dependencies first.
COPY pom.xml ./
RUN mvn -B -ntp -q dependency:go-offline

# Now the sources (schema/seed SQL lives under src/main/resources/db and is baked into the jar).
COPY src ./src
RUN mvn -B -ntp -q clean package -DskipTests \
 && cp target/cv_db-*.jar /build/app.jar

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

# Non-root user.
RUN groupadd --system app && useradd --system --gid app --home /app app

# SQLite data lives on a mounted volume; default path overridable via DB_PATH.
# On first start the full reference DB is unpacked from SEED_DB_PATH (never overwrites).
ENV DB_PATH=/data/cv.db \
    SEED_DB_PATH=/app/seed/cv-seed.db.gz \
    IMAGES_DIR=/data/images \
    SEED_IMAGES_DIR=/app/seed/images \
    SERVER_PORT=2011
RUN mkdir -p /data && chown -R app:app /data /app

COPY --chown=app:app seed/cv-seed.db.gz /app/seed/cv-seed.db.gz
COPY --chown=app:app src/main/resources/images /app/seed/images
COPY --from=build --chown=app:app /build/app.jar /app/app.jar

USER app
EXPOSE 2011
VOLUME ["/data"]

# JVM memory tuning (e.g. -Xmx) is supplied by compose via JAVA_TOOL_OPTIONS, not baked here.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
