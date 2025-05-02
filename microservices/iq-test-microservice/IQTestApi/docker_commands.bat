docker build -t iq_microservice:v1 .
docker run -p 2014:2014 --name iq_microservice --net my-net --ip 172.18.0.14 iq_microservice:v1