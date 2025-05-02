docker build -t manager_db_microservice:v1 .
docker run -p 2015:2015 --name manager_db_microservice --net my-net --ip 172.18.0.15 manager_db_microservice:v1