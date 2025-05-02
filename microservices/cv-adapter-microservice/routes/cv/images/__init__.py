from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted
from utils.rest_consumer.web_utils import consume_get_bytes, consume_update_bytes


@bp.route('/users/<id>/image', methods=['GET'])
# @jwt_required()
def get_image_by_id(id):
    local_url = cv_db_url + 'api/users/{}/image'.format(id)
    try:
        image_content_type_pair, status = consume_get_bytes(local_url, debug=False)
        local_headers = {'Content-type': image_content_type_pair[1]}
        return Response(response=image_content_type_pair[0], headers=local_headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/users/<id>/image', methods=['DELETE'])
@jwt_required()
def delete_image_by_id(id):
    local_url = cv_db_url + 'api/users/{}/image'.format(id)
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/users/<uid>/image', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def update_image_by_id(uid):
    local_url = cv_db_url + 'api/users/{}/image'.format(uid)
    try:
        binary_data = request.files.get('file').read()
        content_type = request.files.get('file').content_type
        data, status = consume_update_bytes(local_url, debug=False, content_type=content_type, data=binary_data)
        return Response(response=data, headers=headers, status=status)
    except AttributeError as e:
        return Response(status=400, headers=headers, response=convert_dict_to_encoded_json({'msg': 'Invalid form data.'}))
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
