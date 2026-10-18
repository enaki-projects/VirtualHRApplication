# cv-adapter gateway: Flask 3 served by gunicorn, plain HTTP on 0.0.0.0:5000.
# No secrets or service IPs are baked in; all config comes from env at runtime
# (see routes/shared/__init__.py). ffmpeg is NOT needed: the service only proxies
# image/CV bytes, it never transcodes media.
FROM python:3.12-slim-bookworm

ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1

WORKDIR /app

# Pure-Python wheels (Flask 3 / gunicorn / PyJWT) install on arm64 and amd64
# without a compiler, so no build toolchain is required on python:3.12-slim.
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip \
    && pip install --no-cache-dir -r requirements.txt

COPY app.py .
COPY utils/ utils/
COPY routes/ routes/

# Writable scratch dir used by the email confirmation flow (workdir/*.txt).
RUN mkdir -p workdir \
    && useradd --create-home --uid 10001 appuser \
    && chown -R appuser:appuser /app
USER appuser

EXPOSE 5000

# 2 workers / 4 threads is plenty for a Raspberry Pi gateway in front of the
# other microservices; tune via GUNICORN_CMD_ARGS in compose if needed.
CMD ["gunicorn", "-b", "0.0.0.0:5000", "-w", "2", "--threads", "4", "--timeout", "120", "app:app"]
