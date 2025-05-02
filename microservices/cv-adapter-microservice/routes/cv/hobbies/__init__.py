from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted


# -------------------------------------HOBBIES ROUTES-------------------------------------
@bp.route('/cv/hobbies', methods=['GET'])
@jwt_required()
def get_hobbies():
    current_url = cv_db_url + 'api/cv/hobbies'
    try:
        hobbies, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(hobbies), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/hobbies', methods=['GET'])
@jwt_required()
def get_hobbies_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/hobbies'
    try:
        hobbies, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(hobbies), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/hobbies/<id>', methods=['GET'])
@jwt_required()
def get_hobby_by_id(id):
    local_url = cv_db_url + 'api/cv/hobbies/' + id
    try:
        hobby, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(hobby), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/hobbies', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_hobby(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    hobby_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/hobbies'
    try:
        response_data, status = consume(local_url, method='POST', data=hobby_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/hobbies/<id>', methods=['PUT'])
@jwt_required()
def update_hobby(id):
    hobby_data = request.json
    local_url = cv_db_url + 'api/cv/hobbies/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=hobby_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/hobbies/<id>', methods=['DELETE'])
@jwt_required()
def delete_hobby(id):
    local_url = cv_db_url + 'api/cv/hobbies/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())