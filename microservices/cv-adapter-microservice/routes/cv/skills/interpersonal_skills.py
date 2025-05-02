from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json


# -------------------------------------INTERPERSONAL SKILLS ROUTES-------------------------------------
@bp.route('/cv/skills/interpersonal', methods=['GET'])
@jwt_required()
def get_interpersonal_skills():
    current_url = cv_db_url + 'api/cv/skills/interpersonal'
    try:
        interpersonal_skills, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(interpersonal_skills), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/skills/interpersonal', methods=['GET'])
@jwt_required()
def get_interpersonal_skills_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/skills/interpersonal'
    try:
        interpersonal_skills, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(interpersonal_skills), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/interpersonal/<id>', methods=['GET'])
@jwt_required()
def get_interpersonal_skills_by_id(id):
    local_url = cv_db_url + 'api/cv/skills/interpersonal/' + id
    try:
        interpersonal_skills, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(interpersonal_skills), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/interpersonal', methods=['POST'])
@jwt_required()
def create_interpersonal_skills():
    interpersonal_skills_data = request.json
    local_url = cv_db_url + 'api/cv/skills/interpersonal'
    try:
        response_data, status = consume(local_url, method='POST', data=interpersonal_skills_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/interpersonal/<id>', methods=['PUT'])
@jwt_required()
def update_interpersonal_skills(id):
    interpersonal_skills_data = request.json
    local_url = cv_db_url + 'api/cv/skills/interpersonal/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=interpersonal_skills_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skills/interpersonal/<id>', methods=['DELETE'])
@jwt_required()
def delete_interpersonal_skills(id):
    local_url = cv_db_url + 'api/cv/skills/interpersonal/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


