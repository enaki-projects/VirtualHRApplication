from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, manager_db_url, headers, consume, convert_dict_to_encoded_json


# -------------------------------------CONNECTION_REQUESTS ROUTES-------------------------------------
@bp.route('/manager/connection-requests/<uid>', methods=['GET'])
@jwt_required()
def get_connection_request_by_id(uid):
    current_url = manager_db_url + 'api/connection-requests/' + uid
    try:
        connection_requests, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(connection_requests), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connection-requests', methods=['GET'])
@jwt_required()
def get_connection_requests():
    current_url = manager_db_url + 'api/connection-requests?' + request.query_string.decode()
    try:
        connection_requests, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(connection_requests), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connection-requests', methods=['POST'])
@jwt_required()
def create_connection_request():
    connection_request_data = request.json
    local_url = manager_db_url + 'api/connection-requests'
    try:
        response_data, status = consume(local_url, method='POST', data=connection_request_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connection-requests/<id>', methods=['PUT'])
@jwt_required()
def update_connection_request(id):
    connection_request_data = request.json
    local_url = manager_db_url + 'api/connection-requests/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=connection_request_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connection-requests/<id>', methods=['DELETE'])
@jwt_required()
def delete_connection_request(id):
    local_url = manager_db_url + 'api/connection-requests/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())