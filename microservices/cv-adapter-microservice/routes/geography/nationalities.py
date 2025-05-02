from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json


# -------------------------------------NATIONALITIES ROUTES-------------------------------------
@bp.route('geography/nationalities', methods=['GET'])
@jwt_required()
def get_nationalities():
    current_url = cv_db_url + 'api/geography/nationalities?' + request.query_string.decode()
    try:
        nationalities, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(nationalities), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/nationalities/<id>', methods=['GET'])
@jwt_required()
def get_nationality_by_id(id):
    local_url = cv_db_url + 'api/geography/nationalities/' + id
    try:
        nationality, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(nationality), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
