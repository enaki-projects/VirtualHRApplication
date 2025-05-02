from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted
# -------------------------------------USER LANGUAGES ROUTES-------------------------------------
from utils import process_user_language


@bp.route('/cv/user-languages', methods=['GET'])
@jwt_required()
def get_user_languages():
    current_url = cv_db_url + 'api/cv/user-languages'
    try:
        user_languages, status = consume(current_url, debug=False)
        for user_language in user_languages:
            process_user_language(user_language)
        return Response(response=convert_dict_to_encoded_json(user_languages), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/user-languages', methods=['GET'])
@jwt_required()
def get_user_languages_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/user-languages'
    try:
        user_languages, status = consume(local_url, debug=False)
        for user_language in user_languages:
            process_user_language(user_language)
        return Response(response=convert_dict_to_encoded_json(user_languages), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/user-languages/<id>', methods=['GET'])
@jwt_required()
def get_user_language_by_id(id):
    local_url = cv_db_url + 'api/cv/user-languages/' + id
    try:
        user_language, status = consume(local_url, debug=False)
        process_user_language(user_language)
        return Response(response=convert_dict_to_encoded_json(user_language), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/user-languages', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_user_language(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    user_language_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/user-languages'
    try:
        response_data, status = consume(local_url, method='POST', data=user_language_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/user-languages/<id>', methods=['PUT'])
@jwt_required()
def update_user_language(id):
    user_language_data = request.json
    local_url = cv_db_url + 'api/cv/user-languages/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=user_language_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/user-languages/<id>', methods=['DELETE'])
@jwt_required()
def delete_user_language(id):
    local_url = cv_db_url + 'api/cv/user-languages/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
