from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, cv_db_url, headers
from routes import consume, convert_dict_to_encoded_json, specific_user_restricted


# -------------------------------------PROJECTS ROUTES-------------------------------------
@bp.route('/cv/projects', methods=['GET'])
@jwt_required()
def get_projects():
    current_url = cv_db_url + 'api/cv/projects'
    try:
        projects, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(projects), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/projects', methods=['GET'])
@jwt_required()
def get_projects_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = cv_db_url + 'api/cv/' + uid + '/projects'
    try:
        projects, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(projects), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/projects/<id>', methods=['GET'])
@jwt_required()
def get_project_by_id(id):
    local_url = cv_db_url + 'api/cv/projects/' + id
    try:
        project, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(project), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/<uid>/projects', methods=['POST'])
@jwt_required()
@specific_user_restricted()
def create_project(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    project_data = request.json
    local_url = cv_db_url + 'api/cv/' + uid + '/projects'
    try:
        response_data, status = consume(local_url, method='POST', data=project_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/projects/<id>', methods=['PUT'])
@jwt_required()
def update_project(id):
    project_data = request.json
    local_url = cv_db_url + 'api/cv/projects/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=project_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/cv/projects/<id>', methods=['DELETE'])
@jwt_required()
def delete_project(id):
    local_url = cv_db_url + 'api/cv/projects/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())