from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted
# -------------------------------------LOCATIONS ROUTES-------------------------------------
from utils import process_location


@bp.route('/cv/locations', methods=['GET'])
@jwt_required()
def get_locations():
    current_url = cv_db_url + 'api/cv/locations'
    try:
        locations, status = consume(current_url, debug=False)
        for location in locations:
            process_location(location)
        return Response(response=convert_dict_to_encoded_json(locations), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/locations', methods=['GET'])
@jwt_required()
def get_locations_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/locations'
    try:
        locations, status = consume(local_url, debug=False)
        for location in locations:
            process_location(location)
        return Response(response=convert_dict_to_encoded_json(locations), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/locations/<id>', methods=['GET'])
@jwt_required()
def get_location_by_id(id):
    local_url = cv_db_url + 'api/cv/locations/' + id
    try:
        location, status = consume(local_url, debug=False)
        process_location(location)
        return Response(response=convert_dict_to_encoded_json(location), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/locations', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_location(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    location_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/locations'
    try:
        response_data, status = consume(local_url, method='POST', data=location_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/locations/<id>', methods=['PUT'])
@jwt_required()
def update_location(id):
    location_data = request.json
    local_url = cv_db_url + 'api/cv/locations/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=location_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/locations/<id>', methods=['DELETE'])
@jwt_required()
def delete_location(id):
    local_url = cv_db_url + 'api/cv/locations/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())