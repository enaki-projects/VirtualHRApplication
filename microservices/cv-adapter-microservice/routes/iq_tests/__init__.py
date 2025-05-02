from datetime import datetime
from urllib.error import HTTPError

from flask import Response, request, json
from flask_jwt_extended import jwt_required

from routes import bp, headers, iq_test_url, cv_db_url
from routes import consume, convert_dict_to_encoded_json
from utils.rest_consumer.web_utils import consume_get_bytes


@bp.route('raven-test', methods=['GET'])
@jwt_required()
def get_iq_questions():
    current_url = iq_test_url + 'api/raven-test?' + request.query_string.decode()
    try:
        iq_questions, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(iq_questions), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('raven-test/<image_code>', methods=['GET'])
#@jwt_required()
def get_iq_tests_by_id(image_code):
    local_url = iq_test_url + 'api/raven-test/{}'.format(image_code)
    try:
        image_content_type_pair, status = consume_get_bytes(local_url, debug=False)
        local_headers = {'Content-type': image_content_type_pair[1]}
        return Response(response=image_content_type_pair[0], headers=local_headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('raven-test', methods=['POST'])
@jwt_required()
def post_iq_test():
    iq_test = request.json
    current_url = iq_test_url + 'api/raven-test'
    try:
        response_data, status = consume(current_url, method='POST', data=iq_test, debug=False)
        full_response_to_post_to_cv_db = response_data.copy()
        full_response_to_post_to_cv_db["userAnswer"] = json.dumps(iq_test)
        # print(json.loads(full_response_to_post_to_cv_db["userAnswer"]))
        return Response(response=convert_dict_to_encoded_json(full_response_to_post_to_cv_db), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('iq-tests', methods=['GET'])
@jwt_required()
def get_iq_test_data():
    current_url = cv_db_url + 'api/iq-tests?' + request.query_string.decode()
    try:
        iq_test_results, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(iq_test_results), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('iq-tests/<id>', methods=['GET'])
@jwt_required()
def get_iq_test_data_by_id(id):
    current_url = cv_db_url + 'api/iq-tests/' + id + request.query_string.decode()
    try:
        iq_test_result, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(iq_test_result), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('iq-tests/<id>', methods=['DELETE'])
@jwt_required()
def delete_iq_test_data_by_id(id):
    current_url = cv_db_url + 'api/iq-tests/' + id + request.query_string.decode()
    try:
        iq_test_result, status = consume(current_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(iq_test_result), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/iq-tests', methods=['GET'])
@jwt_required()
def get_iq_test_data_by_uid(uid):
    current_url = cv_db_url + 'api/users/' + uid + '/iq-tests?' + request.query_string.decode()
    try:
        iq_test_results, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(iq_test_results), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/iq-tests', methods=['POST'])
@jwt_required()
def process_and_add_iq_result(uid):
    iq_test = request.json
    current_url = iq_test_url + 'api/raven-test'
    try:
        response_data, status = consume(current_url, method='POST', data=iq_test, debug=False)
        full_response_to_post_to_cv_db = response_data.copy()
        full_response_to_post_to_cv_db["userAnswer"] = json.dumps(iq_test)
        full_response_to_post_to_cv_db["uid"] = uid
        full_response_to_post_to_cv_db["submittedOn"] = iq_test["Finished"] if "Finished" in iq_test else datetime.now().isoformat()

        current_url = cv_db_url + 'api/iq-tests'
        response_data, status = consume(current_url, method='POST', data=full_response_to_post_to_cv_db, debug=False)

        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers,
                        status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/iq-tests/grant-permission', methods=['POST'])
@jwt_required()
def grant_permission_to_retake_iq_test(uid):
    current_url = cv_db_url + 'api/users/{}/iq-tests/grant-permission'.format(uid)
    try:
        response_data, status = consume(current_url, method='POST', data=None, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers,
                        status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/iq-tests/request-permission', methods=['POST'])
@jwt_required()
def request_permission_to_retake_iq_test(uid):
    current_url = cv_db_url + 'api/users/{}/iq-tests/request-permission'.format(uid)
    try:
        response_data, status = consume(current_url, method='POST', data=None, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers,
                        status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('users/<uid>/iq-tests/restrict-permission', methods=['POST'])
@jwt_required()
def restrict_permission_to_take_iq_test(uid):
    current_url = cv_db_url + 'api/users/{}/iq-tests/restrict-permission'.format(uid)
    try:
        response_data, status = consume(current_url, method='POST', data=None, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers,
                        status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())
