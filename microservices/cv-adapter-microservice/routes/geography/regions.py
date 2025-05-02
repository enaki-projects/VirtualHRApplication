from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json
# -------------------------------------regions ROUTES-------------------------------------
from utils import process_city


@bp.route('geography/regions', methods=['GET'])
@jwt_required()
def get_regions():
    current_url = cv_db_url + 'api/geography/regions?' + request.query_string.decode()
    try:
        regions, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(regions), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/regions/<id>', methods=['GET'])
@jwt_required()
def get_region_by_id(id):
    local_url = cv_db_url + 'api/geography/regions/' + id
    try:
        region, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(region), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/regions/<id>/cities', methods=['GET'])
@jwt_required()
def get_cities_by_region_id(id):
    local_url = cv_db_url + 'api/geography/regions/' + id + '/cities?' + request.query_string.decode()
    try:
        cities, status = consume(local_url, debug=False)
        for city in cities:
            process_city(city)
        return Response(response=convert_dict_to_encoded_json(cities), headers=headers, status=status)

    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())