FROM python:3.9-alpine
RUN mkdir -p /app

WORKDIR /app

ENV FLASK_APP=app.py
ENV FLASK_RUN_HOST=0.0.0.0
ENV AVATAR_DB_MICROSERVICE=172.18.0.10
ENV CV_DB_MICROSERVICE=172.18.0.11
ENV JWT_DB_MICROSERVICE=172.18.0.12
ENV CV_FILTER_MICROSERVICE=172.18.0.13
ENV IQ_TEST_MICROSERVICE=172.18.0.14
ENV MANAGER_DB_MICROSERVICE=172.18.0.15
ENV CANDIDATE_MICROSERVICE=172.18.0.2

ENV SECRET_KEY=******
ENV MAIL_SERVER=smtp.mailgun.org
ENV MAIL_PORT=587
ENV MAIL_USERNAME=******
ENV MAIL_PASSWORD=******

RUN mkdir -p /utils
RUN mkdir -p /routes

COPY requirements.txt .
COPY app.py .
COPY utils/. utils/.
COPY routes/. routes/.
RUN apk add alpine-sdk gcc musl-dev python3-dev libffi-dev openssl-dev cargo jpeg-dev zlib-dev  ffmpeg

RUN pip install --upgrade pip \
    && pip install -r requirements.txt
EXPOSE 5000
CMD ["python3", "app.py"]