docker build -t frontend_admin:v1 .
docker run -p 3002:3000 --name frontend_admin --net my-net --ip 172.18.0.4 frontend_admin:v1
