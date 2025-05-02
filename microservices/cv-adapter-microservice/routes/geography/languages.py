from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json


# -------------------------------------LANGUAGES ROUTES-------------------------------------
@bp.route('geography/languages', methods=['GET'])
@jwt_required()
def get_languages():
    current_url = cv_db_url + 'api/geography/languages?' + request.query_string.decode()
    try:
        languages, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(languages), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('geography/languages/<id>', methods=['GET'])
@jwt_required()
def get_language_by_id(id):
    local_url = cv_db_url + 'api/geography/languages/' + id
    try:
        language, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(language), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
