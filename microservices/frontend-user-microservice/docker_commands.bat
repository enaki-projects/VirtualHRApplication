docker build -t frontend_user:v1 .
docker run -p 3000:3000 --name frontend_user --net my-net --ip 172.18.0.2 frontend_user:v1

:: for firefox in docker in order to access frontend services (cors)
:: docker run -d --name=firefox --net my-net --ip 172.18.0.200 -p 5800:5800 -v /Users/ajeetraina/datas:/config:rw --shm-size 2g jlesage/firefox
