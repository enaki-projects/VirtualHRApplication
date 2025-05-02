from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted


# -------------------------------------PUBLICATIONS ROUTES-------------------------------------
@bp.route('/cv/publications', methods=['GET'])
@jwt_required()
def get_publications():
    current_url = cv_db_url + 'api/cv/publications'
    try:
        publications, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(publications), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/publications', methods=['GET'])
@jwt_required()
def get_publications_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/publications'
    try:
        publications, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(publications), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/publications/<id>', methods=['GET'])
@jwt_required()
def get_publication_by_id(id):
    local_url = cv_db_url + 'api/cv/publications/' + id
    try:
        publication, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(publication), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/publications', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_publication(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    publication_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/publications'
    try:
        response_data, status = consume(local_url, method='POST', data=publication_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/publications/<id>', methods=['PUT'])
@jwt_required()
def update_publication(id):
    publication_data = request.json
    local_url = cv_db_url + 'api/cv/publications/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=publication_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/publications/<id>', methods=['DELETE'])
@jwt_required()
def delete_publication(id):
    local_url = cv_db_url + 'api/cv/publications/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())