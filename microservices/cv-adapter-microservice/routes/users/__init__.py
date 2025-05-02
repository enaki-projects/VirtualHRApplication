from flask_jwt_extended import jwt_required

from urllib.error import HTTPError

from flask import request, Response
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers, restricted
# -------------------------------------USER ROUTES-------------------------------------
from utils import consume, convert_dict_to_encoded_json


def process_role(user):
    roles = []
    for role_url in user["roles"]:
        data, status = consume(role_url)
        roles.append(data)
    return roles


@bp.route('/users', methods=['GET'])
@jwt_required()
@restricted(access_level="administrator")
def get_users():
    local_url = cv_db_url + "api/users"
    try:
        data, status = consume(local_url)
        for idx in range(len(data)):
            data[idx]["roles"] = process_role(data[idx])
            local_url = cv_db_url + 'api/cvs/' + str(data[idx]["uid"])
            cv, status = consume(local_url)
            data[idx]["cv"] = cv
            del data[idx]["cv"]["cvReferences"]
        return Response(response=convert_dict_to_encoded_json(data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/users/<uid>', methods=['GET'])
@jwt_required()
@restricted(access_level="administrator")
def get_user_by_uid(uid):
    local_url = cv_db_url + "api/users/" + uid
    try:
        data, status = consume(local_url)
        data["roles"] = process_role(data)
        local_url = cv_db_url + 'api/cvs/' + uid
        cv, status = consume(local_url)
        data["cv"] = cv
        del data["cv"]["cvReferences"]
        return Response(response=convert_dict_to_encoded_json(data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/grant-role/<role_id>', methods=['POST'])
@jwt_required()
@restricted(access_level="administrator")
def grant_role(uid, role_id):
    current_url = cv_db_url + 'api/users/{}/grant-role/{}'.format(uid, role_id)
    try:
        response_data, status = consume(current_url, method='POST', data=None, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers,
                        status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/revoke-role/<role_id>', methods=['POST'])
@jwt_required()
@restricted(access_level="administrator")
def revoke_role(uid, role_id):
    current_url = cv_db_url + 'api/users/{}/revoke-role/{}'.format(uid, role_id)
    try:
        response_data, status = consume(current_url, method='POST', data=None, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers,
                        status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/users/<uid>/change-password', methods=['PUT'])
@jwt_required()
def change_password(uid):
    user_password = request.json
    local_url = cv_db_url + "api/users/" + uid + '/change-password'
    change_password_data = {
        "password": user_password["password"],
        "oldPassword": user_password["oldPassword"],
    }
    try:
        response_data_user, status = consume(local_url, method='PUT', data=change_password_data)
        return Response(response=convert_dict_to_encoded_json(response_data_user), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())