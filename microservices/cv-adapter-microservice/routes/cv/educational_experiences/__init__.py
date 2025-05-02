from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted
# -------------------------------------EDUCATIONAL EXPERIENCES ROUTES-------------------------------------
from utils import process_educational_experience


@bp.route('/cv/educational-experiences', methods=['GET'])
@jwt_required()
def get_educational_experiences():
    current_url = cv_db_url + 'api/cv/educational-experiences'
    try:
        educational_experiences, status = consume(current_url, debug=False)
        for educational_experience in educational_experiences:
            process_educational_experience(educational_experience)
        return Response(response=convert_dict_to_encoded_json(educational_experiences), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/educational-experiences', methods=['GET'])
@jwt_required()
def get_educational_experiences_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/educational-experiences'
    try:
        educational_experiences, status = consume(local_url, debug=False)
        for educational_experience in educational_experiences:
            process_educational_experience(educational_experience)
        return Response(response=convert_dict_to_encoded_json(educational_experiences), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/educational-experiences/<id>', methods=['GET'])
@jwt_required()
def get_educational_experiences_by_id(id):
    local_url = cv_db_url + 'api/cv/educational-experiences/' + id
    try:
        educational_experience, status = consume(local_url, debug=False)
        process_educational_experience(educational_experience)
        return Response(response=convert_dict_to_encoded_json(educational_experience), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/educational-experiences', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_educational_experiences(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    educational_experience_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/educational-experiences'
    try:
        response_data, status = consume(local_url, method='POST', data=educational_experience_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/educational-experiences/<id>', methods=['PUT'])
@jwt_required()
def update_educational_experiences(id):
    educational_experience_data = request.json
    local_url = cv_db_url + 'api/cv/educational-experiences/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=educational_experience_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/educational-experiences/<id>', methods=['DELETE'])
@jwt_required()
def delete_educational_experiences(id):
    local_url = cv_db_url + 'api/cv/educational-experiences/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())