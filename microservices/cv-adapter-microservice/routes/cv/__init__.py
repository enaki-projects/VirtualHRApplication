import flask_jwt_extended

from routes import bp, cv_db_url, headers, bad_request, restricted, specific_user_restricted, forbidden_error
from utils.rest_consumer.cv_web_utils import process_cv
from .awards import *
from .citizenship import *
from .educational_experiences import *
from .hobbies import *
from .images import *
from .locations import *
from .organisations import *
from .phone_numbers import *
from .professional_experiences import *
from .projects import *
from .publications import *
from .skills import *
from .user_languages import *
from ..shared import cv_filter_url


def is_cv_valid(cv):
    if not cv.get("firstname") or not cv.get("lastname") or not cv.get("email") or not cv.get(
            "birthdate") or not cv.get("gender"):
        return False
    return True


# -------------------------------------CV ROUTES-------------------------------------
@bp.route('/cvs', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cvs():
    current_url = cv_db_url + 'api/cvs'
    try:
        print(current_url)
        data, status = consume(current_url)
        for idx in range(len(data)):
            data[idx] = process_cv(data[idx])
        return Response(response=convert_dict_to_encoded_json(data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cvs/filter', methods=['POST'])
@jwt_required()
@restricted(access_level="manager")
def filter_cvs():
    current_url = cv_db_url + 'api/cvs'
    try:
        print(current_url)
        data, status = consume(current_url)
        for idx in range(len(data)):
            data[idx] = process_cv(data[idx])
        request_data = request.json
        request_data["cvs"] = data

        current_url_for_filter = cv_filter_url + 'filter-cvs'
        filtered_data, status = consume(current_url_for_filter, method='POST', data=request_data)

        return Response(response=convert_dict_to_encoded_json(filtered_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cvs/<uid>', methods=['GET'])
@jwt_required()
def get_cv(uid):
    local_url = cv_db_url + 'api/cvs/' + uid
    if uid is None:
        return bad_request("Uid not specified")
    cv = {}
    try:
        cv, status = consume(local_url)
        cv = process_cv(cv)
        return Response(response=convert_dict_to_encoded_json(cv), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cvs/<uid>', methods=['PUT'])
@jwt_required()
@specific_user_restricted()
def update_cv(uid):
    if uid != flask_jwt_extended.get_jwt_identity():
        return Response(status=403, response=convert_dict_to_encoded_json(forbidden_error), headers=headers)
    cv_data = request.json
    if not is_cv_valid(cv_data):
        return bad_request("Invalid Form")

    local_url = cv_db_url + 'api/cvs/' + uid
    try:
        response_data, status = consume(local_url, method='PUT', data=cv_data)
        print(response_data)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


