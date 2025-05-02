docker build -t cv_adapter_microservice:v1 .
:: docker run -p 5000:5000 --name cv_adapter_microservice cv_adapter_microservice:v1
docker run -p 5000:5000 --name cv_adapter_microservice --net my-net --ip 172.18.0.50 cv_adapter_microservice:v1