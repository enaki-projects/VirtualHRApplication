import urllib
from urllib import parse
from urllib.error import HTTPError

from flask import Response, request
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, convert_dict_to_encoded_json, consume, headers


@bp.route('/cv/<id>/<skillType>', methods=['DELETE'])
@jwt_required()
def delete_user_interpersonal_skills(id, skillType):
    local_url = cv_db_url + 'api/cv/' + id + '/' + skillType + '/?' + request.query_string.decode()
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<id>/<skillType>', methods=['POST'])
@jwt_required()
def add_user_interpersonal_skills(id, skillType):
    local_url = cv_db_url + 'api/cv/' + id + '/' + skillType + '/?' + request.query_string.decode()
    try:
        response_data, status = consume(local_url, method='POST', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/skill-by-name/<skillType>/<skillName>', methods=['GET'])
@jwt_required()
def get_user_interpersonal_skill_by_name(skillType, skillName):
    local_url = cv_db_url + 'api/cv/skill-by-name/{}/{}'.format(skillType, urllib.parse.quote(skillName))
    try:
        response_data, status = consume(local_url, method='GET', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())