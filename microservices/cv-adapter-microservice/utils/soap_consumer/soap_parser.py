from utils.xml_parser import xml_to_dict


def parse_auth_login_env(xml_envelope):
    data = xml_to_dict(xml_envelope)
    env_details = data["{http://schemas.xmlsoap.org/soap/envelope/}Body"]['{http://spring.io/guides/gs-producing-web-service}getAuthResponse']['{http://spring.io/guides/gs-producing-web-service}user']
    info = env_details['{http://spring.io/guides/gs-producing-web-service}uid']
    token = env_details['{http://spring.io/guides/gs-producing-web-service}token']
    email = env_details['{http://spring.io/guides/gs-producing-web-service}email']

    return {
               "uid": info,
               "email": email,
               "token": token
           }, 200


def parse_jwt_decoder_env(xml_envelope):
    data = xml_to_dict(xml_envelope)
    env_details = data["{http://schemas.xmlsoap.org/soap/envelope/}Body"]['{http://spring.io/guides/gs-producing-web-service}getAuthResponse']['{http://spring.io/guides/gs-producing-web-service}user']
    info = env_details['{http://spring.io/guides/gs-producing-web-service}uid']
    token = env_details['{http://spring.io/guides/gs-producing-web-service}token']
    email = env_details['{http://spring.io/guides/gs-producing-web-service}email']
    return {
               "uid": info,
               "email": email,
               "token": token
           }, 200


def parse_fault(xml_envelope):
    data = xml_to_dict(xml_envelope)
    env_details = data["{http://schemas.xmlsoap.org/soap/envelope/}Body"]["{http://schemas.xmlsoap.org/soap/envelope/}Fault"]["detail"]
    info = env_details["code"]
    message = env_details["description"]
    status_code = int(info.split()[0])
    return {
        "info": info,
        "message": message
    }, status_code