from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json
# -------------------------------------ORGANISATIONS ROUTES-------------------------------------
from utils import process_organisation


@bp.route('/organisations', methods=['GET'])
@jwt_required()
def get_organisations():
    current_url = cv_db_url + 'api/organisations?' + request.query_string.decode()
    try:
        organisations, status = consume(current_url, debug=False)
        for organisation in organisations:
            process_organisation(organisation)
        return Response(response=convert_dict_to_encoded_json(organisations), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/organisations/<id>', methods=['GET'])
@jwt_required()
def get_organisation_by_id(id):
    local_url = cv_db_url + 'api/organisations/' + id
    try:
        organisation, status = consume(local_url, debug=False)
        process_organisation(organisation)
        return Response(response=convert_dict_to_encoded_json(organisation), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/organisations', methods=['POST'])
@jwt_required()
def create_organisation():
    organisation_data = request.json
    local_url = cv_db_url + 'api/organisations'
    try:
        response_data, status = consume(local_url, method='POST', data=organisation_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/organisations/<id>', methods=['PUT'])
@jwt_required()
def update_organisation(id):
    organisation_data = request.json
    local_url = cv_db_url + 'api/organisations/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=organisation_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/organisations/<id>', methods=['DELETE'])
@jwt_required()
def delete_organisation(id):
    local_url = cv_db_url + 'api/organisations/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/organisations-by-city/<id>', methods=['GET'])
@jwt_required()
def get_organisations_by_city(id):
    current_url = cv_db_url + 'api/organisations-by-city/' + id + '?' + request.query_string.decode()
    try:
        organisations, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(organisations), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())