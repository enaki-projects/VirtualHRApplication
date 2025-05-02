:: pip3 install -r requirements.txt

python3 generate_avatar_xml.py

copy /y "output\avatar.xml" "..\..\microservices\avatar-db-microservice\src\main\resources\avatar.xml"

copy /y "output\languages\*" "..\..\microservices\avatar-db-microservice\src\main\resources\available_languages\*"
