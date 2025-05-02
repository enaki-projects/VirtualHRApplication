from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted


# -------------------------------------USER LANGUAGES ROUTES-------------------------------------


@bp.route('/cv/<uid>/citizenship', methods=['GET'])
@jwt_required()
def get_user_nationalities_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/citizenship'
    try:
        nationality_list = []
        nationalities, status = consume(local_url, debug=False)
        for nationality_url in nationalities:
            citizenship, status = consume(nationality_url, debug=False)
            nationality_list.append(citizenship)
        return Response(response=convert_dict_to_encoded_json(nationality_list), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/citizenship/<nationality_id>', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_user_nationality(uid, nationality_id):
    if uid is None:
        return abort(400, "Uid not specified")
    user_language_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/citizenship/' + nationality_id
    try:
        response_data, status = consume(local_url, method='POST', data=user_language_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/citizenship/<nationality_id>', methods=['DELETE'])
@jwt_required()
@specific_user_restricted()
def delete_user_nationality(uid, nationality_id):
    local_url = cv_db_url + 'api/cv/' + uid + '/citizenship/' + nationality_id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
