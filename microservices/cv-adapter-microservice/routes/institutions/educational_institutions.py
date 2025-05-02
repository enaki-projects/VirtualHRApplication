from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json
# -------------------------------------CITIES ROUTES-------------------------------------
from utils import process_educational_institution


@bp.route('institutions/educational', methods=['GET'])
@jwt_required()
def get_educational_institutions():
    current_url = cv_db_url + 'api/institutions/educational?' + request.query_string.decode()
    try:
        educational_institutions, status = consume(current_url, debug=False)
        for educational_institution in educational_institutions:
            process_educational_institution(educational_institution)
        return Response(response=convert_dict_to_encoded_json(educational_institutions), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('institutions/educational-basic', methods=['GET'])
@jwt_required()
def get_educational_institutions_basic():
    current_url = cv_db_url + 'api/institutions/educational-basic?' + request.query_string.decode()
    try:
        educational_institutions, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(educational_institutions), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('institutions/educational-by-country/<id>', methods=['GET'])
@jwt_required()
def get_educational_institutions_by_country(id):
    current_url = cv_db_url + 'api/institutions/educational-by-country/' + id + '?' + request.query_string.decode()
    try:
        educational_institutions, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(educational_institutions), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('institutions/educational/<id>', methods=['GET'])
@jwt_required()
def get_educational_institution_by_id(id):
    local_url = cv_db_url + 'api/institutions/educational/' + id
    try:
        educational_institution, status = consume(local_url, debug=False)
        process_educational_institution(educational_institution)
        return Response(response=convert_dict_to_encoded_json(educational_institution), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
