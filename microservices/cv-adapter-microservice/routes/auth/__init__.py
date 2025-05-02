import hashlib
import smtplib
import urllib
from functools import wraps
from urllib import parse
from urllib.error import HTTPError, URLError

import flask_jwt_extended
from flask import request, Response, abort
from flask_mail import Mail, Message

from routes import bp, cv_db_url, headers, bad_request, get_soap_auth_envelope, parse_auth_login_env, soap_consume, \
    parse_fault, convert_dict_to_encoded_json, consume
from routes.shared import secret_key, candidate_url
from .auth_utils import *

mail = Mail()

forbidden_error = {
    "status": 403,
    "error": "Unauthorised",
    "message": "This user is not authorised",
}


def restricted(access_level):
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            if not access_level in get_roles(flask_jwt_extended.get_jwt()):
                return Response(status=403,
                                response=convert_dict_to_encoded_json(forbidden_error),
                                headers=headers)
            return func(*args, **kwargs)
        return wrapper
    return decorator


def specific_user_restricted():
    def decorator(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            print(*args)
            if "uid" in kwargs and kwargs["uid"] != flask_jwt_extended.get_jwt_identity():
                return Response(status=403,
                                response=convert_dict_to_encoded_json(forbidden_error),
                                headers=headers)
            return func(*args, **kwargs)
        return wrapper
    return decorator


with open("routes/auth/templates/register_template.txt") as f:
    register_template = f.read()
with open("routes/auth/templates/recover_template.txt") as f:
    recover_template = f.read()


def get_roles(user):
    if 'roles' in user:
        return list(map(lambda x: x["roleName"], user["roles"]))
    return []


def has_role(data, role):
    if "token" in data:
        return role in get_roles(flask_jwt_extended.decode_token(data["token"]))
    return False


def has_role(data, role):
    if "token" in data:
        return role in list(map(lambda x: x["roleName"], flask_jwt_extended.decode_token(data["token"])["roles"]))
    return False


def hash_text(text):
    return hashlib.sha256(text.encode() + secret_key.encode()).hexdigest()


def is_auth_valid(user):
    if not user.get("firstname") or not user.get("lastname") or not user.get("email") or not user.get(
            "birthdate") or not user.get("gender") or not user.get("password") or len(user.get('password')) < 6:
        return False
    return True


# -------------------------------------AUTH ROUTES-------------------------------------
@bp.route('/auth/login', methods=['POST'])
def login():
    user = request.json
    if not user or not user.get("email") or not user.get("password"):
        return bad_request("Require email and password")
    try:
        soap_envelope = get_soap_auth_envelope(user['email'], user['password'])
        response_data, status = soap_consume(soap_envelope.encode())
        if status != 200:
            data, parsed_status = parse_fault(response_data)
            return Response(status=parsed_status, response=convert_dict_to_encoded_json(data), headers=headers)

        data, parsed_status = parse_auth_login_env(response_data)
        role = request.args.get('as')
        if role and not has_role(data, role):
            return Response(status=403, response=convert_dict_to_encoded_json(forbidden_error), headers=headers)

        return Response(status=parsed_status, response=convert_dict_to_encoded_json(data), headers=headers)
    except KeyError as e:
        return bad_request("Email or password is missing")
    except URLError as e:
        return bad_request("Auth microservice is down")
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/auth/login', methods=['POST'])
def login_manager():
    print(request.json)
    user = request.json
    try:
        soap_envelope = get_soap_auth_envelope(user['email'], user['password'])
        response_data, status = soap_consume(soap_envelope.encode())
        if status != 200:
            data, parsed_status = parse_fault(response_data)
            return Response(status=parsed_status, response=convert_dict_to_encoded_json(data), headers=headers)

        data, parsed_status = parse_auth_login_env(response_data)
        return Response(status=parsed_status, response=convert_dict_to_encoded_json(data), headers=headers)
    except KeyError as e:
        return bad_request("Email or password is missing")
    except URLError as e:
        return bad_request("Auth microservice is down")
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


# REGISTER
@bp.route('/auth/register', methods=['POST'])
def register():
    user = request.json
    print(user)
    current_action = 'register'
    if not is_auth_valid(user):
        return abort(400, "Invalid Form")
    try:
        local_url = cv_db_url + "api/users?email=" + urllib.parse.quote(user["email"])
        response_data, status = consume(local_url, method='GET')
        error_response = {
            "status": 400,
            "error": "User with value field (email): ({}) is existent".format(user["email"])
        }
        return Response(status=400, response=convert_dict_to_encoded_json(error_response), headers=headers)

    except HTTPError as e:
        # user does not exist so registration may continue
        pass

    try:
        hash_email = hash_text(user["email"])
        if exist_action(hash_email, action=current_action):
            error_response = {
                "status": 400,
                "error": "Check Your Email. Registration already submitted."
            }
            return Response(status=400, response=convert_dict_to_encoded_json(error_response), headers=headers)

        add_action(hash_email, data=user, action=current_action)

        msg = Message("Confirm Registration VirtualHR",
                      sender="VirtualHR@license.tuiasi.ro",
                      recipients=[user["email"]])
        msg.html = register_template.format("{}".format(user["firstname"]),
                                            request.url + '/confirm/' + urllib.parse.quote(hash_email))
        mail.send(msg)
        return Response(status=202, headers=headers)
    except Exception as e:
        try:
            delete_action(hash_email, current_action)
            response_data_cv, status = register_user_func(user)
            return Response(status=status, response=convert_dict_to_encoded_json(response_data_cv), headers=headers)
        except HTTPError as e:
            return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/auth/register/confirm/<token>', methods=['GET'])
def confirm_register(token):
    current_action = 'register'
    if exist_action(token, current_action):
        try:
            data = read_action(token, current_action)
            response_data_cv, status = register_user_func(data)
        except HTTPError as e:
            return Response(status=e.code, headers=headers, response=e.read())
        finally:
            delete_action(token, current_action)
        if status == 201:
            return Response(status=status, response="Registration Successful", headers=headers)
        return Response(status=status, response=convert_dict_to_encoded_json(response_data_cv), headers=headers)
    return Response(status=400, response="Already Confirmed Or Invalid", headers=headers)


def register_user_func(user):
    local_url = cv_db_url + "api/users"
    register_user = {
        "email": user["email"],
        "password": user["password"],
    }
    response_data_user, status = consume(local_url, method='POST', data=register_user)
    register_cv = {
        "uid": response_data_user["Id"],
        "firstname": user["firstname"],
        "lastname": user["lastname"],
        "email": user["email"],
        "gender": user["gender"],
        "birthdate": user["birthdate"],
    }
    local_url = cv_db_url + "api/cvs"
    response_data_cv, status = consume(local_url, method='POST', data=register_cv)
    return response_data_cv, status


# RECOVER
@bp.route('/auth/recover', methods=['POST'])
def request_recover():
    current_action = 'recover'
    user = request.json
    if not user.get("email"):
        return abort(400, "Invalid Form")
    try:
        local_url = cv_db_url + "api/users?email=" + urllib.parse.quote(user["email"])
        response_data, status = consume(local_url, method='GET')
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())

    try:
        hash_email = hash_text(user["email"])
        if exist_action(hash_email, action=current_action):
            error_response = {
                "status": 400,
                "error": "Check Your Email. Recover already submitted."
            }
            return Response(status=400, response=convert_dict_to_encoded_json(error_response), headers=headers)

        add_action(hash_email, data={
            "uid": response_data["uid"],
            "email": response_data["email"],
        }, action=current_action)

        msg = Message("Confirm Password Recover VirtualHR",
                      sender="VirtualHR@license.tuiasi.ro",
                      recipients=[user["email"]])
        msg.html = recover_template.format("{}".format(response_data["email"]),
                                            candidate_url + 'auth/recover?token=' + urllib.parse.quote(hash_email))
        mail.send(msg)

        return Response(status=202, headers=headers)
    except smtplib.SMTPDataError as e:
        delete_action(hash_email, current_action)
        error_response = {
            "status": 400,
            "error": "Email Not Found."
        }
        return Response(status=404, response=convert_dict_to_encoded_json(error_response), headers=headers)
    except Exception as e:
        return Response(status=500, response="SMTP Server is Down", headers=headers)


@bp.route('/auth/recover/confirm/<token>', methods=['POST'])
def confirm_recover(token):
    current_action = 'recover'
    user = request.json
    if not user.get("password") or len(user.get('password')) < 6:
        error_response = {
            "status": 400,
            "error": "Invalid Token."
        }
        return Response(status=404, response=convert_dict_to_encoded_json(error_response), headers=headers)

    if exist_action(token, action=current_action):
        try:
            data = read_action(token, 'recover')
            local_url = cv_db_url + "api/users/" + str(data["uid"])

            data["password"] = user["password"]
            response_data, status = consume(local_url, method='PUT', data=data)

        except HTTPError as e:
            return Response(status=e.code, headers=headers, response=e.read())
        finally:
            delete_action(token, 'recover')
        return Response(status=status, response=convert_dict_to_encoded_json(response_data), headers=headers)
    return Response(status=400, response="Already Confirmed Or Invalid", headers=headers)
