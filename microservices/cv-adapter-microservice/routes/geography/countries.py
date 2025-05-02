from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json
# -------------------------------------COUNTRIES ROUTES-------------------------------------
from utils import process_city, process_region


@bp.route('geography/countries', methods=['GET'])
@jwt_required()
def get_countries():
    current_url = cv_db_url + 'api/geography/countries?' + request.query_string.decode()
    try:
        countries, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(countries), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/countries/<id>', methods=['GET'])
@jwt_required()
def get_country_by_id(id):
    local_url = cv_db_url + 'api/geography/countries/' + id
    try:
        country, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(country), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/countries/<id>/cities', methods=['GET'])
@jwt_required()
def get_cities_by_country_id(id):
    local_url = cv_db_url + 'api/geography/countries/' + id + '/cities?' + request.query_string.decode()
    try:
        cities, status = consume(local_url, debug=False)
        for city in cities:
            process_city(city)
        return Response(response=convert_dict_to_encoded_json(cities), headers=headers, status=status)

    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/countries/<id>/regions', methods=['GET'])
@jwt_required()
def get_regions_by_country_id(id):
    local_url = cv_db_url + 'api/geography/countries/' + id + '/regions?' + request.query_string.decode()
    try:
        regions, status = consume(local_url, debug=False)
        for region in regions:
            process_region(region)
        return Response(response=convert_dict_to_encoded_json(regions), headers=headers, status=status)

    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())