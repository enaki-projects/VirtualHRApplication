from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json


# -------------------------------------ORGANISATIONAL SKILLS ROUTES-------------------------------------
@bp.route('/cv/skills/organisational', methods=['GET'])
@jwt_required()
def get_organisational_skills():
    current_url = cv_db_url + 'api/cv/skills/organisational'
    try:
        organisational_skills, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(organisational_skills), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/skills/organisational', methods=['GET'])
@jwt_required()
def get_organisational_skills_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/skills/organisational'
    try:
        organisational_skills, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(organisational_skills), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/organisational/<id>', methods=['GET'])
@jwt_required()
def get_organisational_skills_by_id(id):
    local_url = cv_db_url + 'api/cv/skills/organisational/' + id
    try:
        organisational_skills, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(organisational_skills), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/organisational', methods=['POST'])
@jwt_required()
def create_organisational_skills():
    organisational_skills_data = request.json
    local_url = cv_db_url + 'api/cv/skills/organisational'
    try:
        response_data, status = consume(local_url, method='POST', data=organisational_skills_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/organisational/<id>', methods=['PUT'])
@jwt_required()
def update_organisational_skills(id):
    organisational_skills_data = request.json
    local_url = cv_db_url + 'api/cv/skills/organisational/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=organisational_skills_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/organisational/<id>', methods=['DELETE'])
@jwt_required()
def delete_organisational_skills(id):
    local_url = cv_db_url + 'api/cv/skills/organisational/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())