# cv-filter: Flask 3 + PySpark 3.5 served by gunicorn on 0.0.0.0:2013.
# PySpark 3.5 runs its driver/executor JVM in-process in local mode and supports
# Java 17, so a headless JRE 17 is installed. No separate Spark cluster is needed
# by default (SPARK_MASTER=local[1]); set SPARK_MASTER to use the optional cluster.
FROM python:3.12-slim-bookworm

ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    SPARK_MASTER=local[1]

# openjdk-17-jre-headless is available for arm64 and amd64 in Debian bookworm.
# procps provides `ps`, which Spark's launcher scripts expect.
RUN apt-get update \
    && apt-get install -y --no-install-recommends openjdk-17-jre-headless procps \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements.txt

COPY app.py .

RUN useradd --create-home --uid 10001 appuser && chown -R appuser:appuser /app
USER appuser

EXPOSE 2013

# JAVA_HOME is resolved at runtime (arm64/amd64 install dirs differ). A single
# gunicorn worker: each worker spins up its own JVM/SparkSession, heavy on a Pi.
# Long timeout covers Spark job latency.
CMD ["sh", "-c", "export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which java)))); exec gunicorn -b 0.0.0.0:2013 -w 1 --timeout 600 app:app"]
