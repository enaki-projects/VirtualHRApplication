docker build -t jwt_provider_microservice:v1 .
docker run -p 2012:2012 --name jwt_provider_microservice --net my-net --ip 172.18.0.12 jwt_provider_microservice:v1