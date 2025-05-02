from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, manager_db_url, headers, consume, convert_dict_to_encoded_json


# -------------------------------------CONNECTIONS ROUTES-------------------------------------
@bp.route('/manager/connections/<uid>', methods=['GET'])
@jwt_required()
def get_connection_by_id(uid):
    current_url = manager_db_url + 'api/connections/' + uid
    try:
        connections, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(connections), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connections', methods=['GET'])
@jwt_required()
def get_connections():
    current_url = manager_db_url + 'api/connections'
    try:
        connections, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(connections), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connections/', methods=['GET'])
@jwt_required()
def get_connection_with_params():
    current_url = manager_db_url + 'api/connections/?' + request.query_string.decode()
    try:
        connections, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(connections), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/user/<uid>/connections', methods=['GET'])
@jwt_required()
def get_connections_by_uid(uid):
    current_url = manager_db_url + 'api/user/' + uid + '/connections'
    try:
        connections, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(connections), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connections', methods=['POST'])
@jwt_required()
def create_connection():
    connection_data = request.json
    local_url = manager_db_url + 'api/connections'
    try:
        response_data, status = consume(local_url, method='POST', data=connection_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connections/<id>', methods=['PUT'])
@jwt_required()
def update_connection(id):
    connection_data = request.json
    local_url = manager_db_url + 'api/connections/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=connection_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/connections/<id>', methods=['DELETE'])
@jwt_required()
def delete_connection(id):
    local_url = manager_db_url + 'api/connections/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
