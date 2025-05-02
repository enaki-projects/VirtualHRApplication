docker build -t cv_db_microservice:v1 .
docker run -p 2011:2011 --name cv_db_microservice --net my-net --ip 172.18.0.11 cv_db_microservice:v1