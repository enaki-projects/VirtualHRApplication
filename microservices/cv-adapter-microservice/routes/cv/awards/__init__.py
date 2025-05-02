from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers, consume, convert_dict_to_encoded_json, specific_user_restricted


# -------------------------------------AWARDS ROUTES-------------------------------------
@bp.route('/cv/awards', methods=['GET'])
@jwt_required()
def get_awards():
    current_url = cv_db_url + 'api/cv/awards'
    try:
        awards, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(awards), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/awards', methods=['GET'])
@jwt_required()
def get_awards_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/awards'
    try:
        awards, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(awards), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/awards/<id>', methods=['GET'])
@jwt_required()
def get_award_by_id(id):
    local_url = cv_db_url + 'api/cv/awards/' + id
    try:
        award, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(award), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/awards', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_award(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    award_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/awards'
    try:
        response_data, status = consume(local_url, method='POST', data=award_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/awards/<id>', methods=['PUT'])
@jwt_required()
def update_award(id):
    award_data = request.json
    local_url = cv_db_url + 'api/cv/awards/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=award_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/awards/<id>', methods=['DELETE'])
@jwt_required()
def delete_award(id):
    local_url = cv_db_url + 'api/cv/awards/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())