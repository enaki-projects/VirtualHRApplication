docker build -t frontend_manager:v1 .
docker run -p 3001:3000 --name frontend_manager --net my-net --ip 172.18.0.3 frontend_manager:v1
