docker build -t avatar_db_microservice:v1 .
docker run -p 2010:2010 --name avatar_db_microservice --net my-net --ip 172.18.0.10 avatar_db_microservice:v1
::docker run -p 2010:2010 --name avatar_db_microservice avatar_db_microservice:v1