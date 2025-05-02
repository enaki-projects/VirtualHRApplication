from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted
# -------------------------------------PHONE NUMBERS ROUTES-------------------------------------
from utils import process_phone_number


@bp.route('/cv/phone-numbers', methods=['GET'])
@jwt_required()
def get_phone_numbers():
    current_url = cv_db_url + 'api/cv/phone-numbers'
    try:
        phone_numbers, status = consume(current_url, debug=False)
        for phone_number in phone_numbers:
            process_phone_number(phone_number)
        return Response(response=convert_dict_to_encoded_json(phone_numbers), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/phone-numbers', methods=['GET'])
@jwt_required()
@specific_user_restricted()
def get_phone_numbers_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/phone-numbers'
    try:
        phone_numbers, status = consume(local_url, debug=False)
        for phone_number in phone_numbers:
            process_phone_number(phone_number)
        return Response(response=convert_dict_to_encoded_json(phone_numbers), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/phone-numbers/<id>', methods=['GET'])
@jwt_required()
def get_phone_number_by_id(id):
    local_url = cv_db_url + 'api/cv/phone-numbers/' + id
    try:
        phone_number, status = consume(local_url, debug=False)
        process_phone_number(phone_number)
        return Response(response=convert_dict_to_encoded_json(phone_number), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/phone-numbers', methods=['POST'])
@jwt_required()
def create_phone_number(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    phone_number_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/phone-numbers'
    try:
        response_data, status = consume(local_url, method='POST', data=phone_number_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/phone-numbers/<id>', methods=['PUT'])
@jwt_required()
def update_phone_number(id):
    phone_number_data = request.json
    local_url = cv_db_url + 'api/cv/phone-numbers/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=phone_number_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/phone-numbers/<id>', methods=['DELETE'])
@jwt_required()
def delete_phone_number(id):
    local_url = cv_db_url + 'api/cv/phone-numbers/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())