from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json


# -------------------------------------CITIES ROUTES-------------------------------------


@bp.route('geography/cities', methods=['GET'])
@jwt_required()
def get_cities():
    current_url = cv_db_url + 'api/geography/cities?' + request.query_string.decode()
    try:
        cities, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cities), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/cities/<id>', methods=['GET'])
@jwt_required()
def get_city_by_id(id):
    local_url = cv_db_url + 'api/geography/cities/' + id
    try:
        city, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(city), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
