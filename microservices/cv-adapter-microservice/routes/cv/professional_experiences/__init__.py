from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, bad_request, specific_user_restricted
# -------------------------------------PROFESSIONAL EXPERIENCES ROUTES-------------------------------------
from utils import process_professional_experience


@bp.route('/cv/professional-experiences', methods=['GET'])
@jwt_required()
def get_professional_experiences():
    current_url = cv_db_url + 'api/cv/professional-experiences'
    try:
        professional_experiences, status = consume(current_url, debug=False)
        for professional_experience in professional_experiences:
            process_professional_experience(professional_experience)
        return Response(response=convert_dict_to_encoded_json(professional_experiences), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/professional-experiences', methods=['GET'])
@jwt_required()
def get_professional_experiences_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/professional-experiences'
    try:
        professional_experiences, status = consume(local_url, debug=False)
        for professional_experience in professional_experiences:
            process_professional_experience(professional_experience)
        return Response(response=convert_dict_to_encoded_json(professional_experiences), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/professional-experiences/<id>', methods=['GET'])
@jwt_required()
def get_professional_experiences_by_id(id):
    local_url = cv_db_url + 'api/cv/professional-experiences/' + id
    try:
        professional_experience, status = consume(local_url, debug=False)
        process_professional_experience(professional_experience)
        return Response(response=convert_dict_to_encoded_json(professional_experience), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/professional-experiences', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_professional_experience(uid):
    if uid is None:
        return bad_request("Uid not specified")
    professional_experience_data = request.json
    if not professional_experience_data.get("cityId") and not professional_experience_data.get("organisationName"):
        return bad_request("cityId and organisationName not specified")
    cityId = professional_experience_data["cityId"]
    organisationName = professional_experience_data["organisationName"]

    organisationUrl = '{}api/organisations?name={}&cityId={}'.format(cv_db_url, organisationName.replace(' ', '%20'), cityId)
    organisation_id = None
    try:
        organisations, status = consume(organisationUrl, debug=False)
        if len(organisations) == 0:
            organisationPostUrl = cv_db_url + 'api/organisations'
            data = {
                "cityId": cityId,
                "organisationName": organisationName,
            }
            organisation_response, status = consume(organisationPostUrl, method='POST', data=data, debug=False)
            organisation_id = organisation_response["Id"]
        else:
            # exista deja organizatia
            organisation_id = organisations[0]["organisationId"]
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())

    local_url = cv_db_url + 'api/cv/' + uid + '/professional-experiences'
    try:
        professional_experience_data["organisationId"] = organisation_id
        response_data, status = consume(local_url, method='POST', data=professional_experience_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/professional-experiences/<id>', methods=['PUT'])
@jwt_required()
def update_professional_experiences(id):
    professional_experience_data = request.json
    if not professional_experience_data.get("cityId") and not professional_experience_data.get("organisationName"):
        return bad_request("cityId and organisationName not specified")
    cityId = professional_experience_data["cityId"]
    organisationName = professional_experience_data["organisationName"]

    organisationUrl = '{}api/organisations?name={}&cityId={}'.format(cv_db_url, organisationName.replace(' ', '%20'), cityId)
    organisation_id = None
    try:
        organisations, status = consume(organisationUrl, debug=False)
        if len(organisations) == 0:
            organisationPostUrl = cv_db_url + 'api/organisations'
            data = {
                "cityId": cityId,
                "organisationName": organisationName,
            }
            organisation_response, status = consume(organisationPostUrl, method='POST', data=data, debug=False)
            organisation_id = organisation_response["Id"]
        else:
            # exista deja organizatia
            organisation_id = organisations[0]["organisationId"]
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())

    local_url = cv_db_url + 'api/cv/professional-experiences/' + id
    try:
        professional_experience_data["organisationId"] = organisation_id
        response_data, status = consume(local_url, method='PUT', data=professional_experience_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/professional-experiences/<id>', methods=['DELETE'])
@jwt_required()
def delete_professional_experiences(id):
    local_url = cv_db_url + 'api/cv/professional-experiences/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
