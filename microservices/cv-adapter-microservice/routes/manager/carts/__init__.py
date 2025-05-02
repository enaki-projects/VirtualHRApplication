from urllib.error import HTTPError

import flask_jwt_extended
from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, manager_db_url, headers, consume, convert_dict_to_encoded_json, cv_db_url, restricted, \
    specific_user_restricted


# -------------------------------------CARTS ROUTES-------------------------------------


@bp.route('/manager/carts', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_carts():
    current_url = manager_db_url + 'api/carts'
    try:
        carts, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(carts), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/<uid>/carts', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_carts_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = manager_db_url + 'api/users/' + uid + '/carts'
    try:
        carts, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(carts), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/<uid>/carts/with-candidate/<candidate_id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
@specific_user_restricted()
def get_carts_by_uid_which_contains_candidate(uid, candidate_id):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = manager_db_url + 'api/users/' + uid + '/carts/with-candidate/' + candidate_id
    try:
        carts, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(carts), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/<uid>/carts/with-maintainer/<maintainer_id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
@specific_user_restricted()
def get_carts_by_uid_with_maintainer_status(uid, maintainer_id):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = manager_db_url + 'api/users/' + uid + '/carts/with-maintainer/' + maintainer_id
    try:
        carts, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(carts), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/carts/<id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_by_id(id):
    local_url = manager_db_url + 'api/carts/' + id
    try:
        cart, status = consume(local_url, debug=False)
        cart = process_cart(cart)
        return Response(response=convert_dict_to_encoded_json(cart), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/carts', methods=['POST'])
@jwt_required()
@restricted(access_level="manager")
def create_cart():
    cart_data = request.json
    local_url = manager_db_url + 'api/carts'
    try:
        response_data, status = consume(local_url, method='POST', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/carts/<id>', methods=['PUT'])
@jwt_required()
@restricted(access_level="manager")
def update_cart(id):
    cart_data = request.json
    local_url = manager_db_url + 'api/carts/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/carts/<id>', methods=['DELETE'])
@jwt_required()
@restricted(access_level="manager")
def delete_cart(id):
    try:
        local_url = manager_db_url + 'api/carts/' + id
        cart, status = consume(local_url, debug=False)

        uid = flask_jwt_extended.get_jwt_identity()
        if uid == str(cart["creatorId"]):
            local_url = manager_db_url + 'api/carts/' + id
        else:
            local_url = manager_db_url + 'api/carts/' + id + '/maintainer/' + uid
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/carts/<id>/candidate/<candidate_id>', methods=['DELETE'])
@jwt_required()
@restricted(access_level="manager")
def delete_candidate_from_cart(id, candidate_id):
    local_url = manager_db_url + 'api/carts/' + id + '/candidate/' + candidate_id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/carts/<id>/maintainer/<maintainer_id>', methods=['DELETE'])
@jwt_required()
@restricted(access_level="manager")
def delete_maintainer_from_cart(id, maintainer_id):
    local_url = manager_db_url + 'api/carts/' + id + '/maintainer/' + maintainer_id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


def process_cart(cart):
    for key in cart["references"].keys():
        cart[key] = []
        element_list = []
        if cart["references"][key]:
            for current_url in cart["references"][key]:
                element_data, _ = consume(current_url)
                element_list.append(element_data)
        if key == "items":
            for element in element_list:
                cv_item = fetch_cv(element["candidateId"])
                del cv_item["cvReferences"]
                cart["items"].append({
                    "cartItemId": element["cartItemId"],
                    "cartId": element["cartId"],
                    "candidateId": element["candidateId"],
                    "score": element["score"],
                    "notes": element["notes"],
                    "candidate": cv_item,
                })
        if key == "maintainers":
            for element in element_list:
                cv_maintainer = fetch_cv(element["maintainerId"])
                del cv_maintainer["cvReferences"]
                cart["maintainers"].append(
                    {
                        "cartMaintainerId": element["cartMaintainerId"],
                        "cartId": element["cartId"],
                        "maintainerId": element["maintainerId"],
                        "role": element["role"],
                        "maintainer": cv_maintainer,
                    })
    del cart["references"]

    return cart


def fetch_cv(candidate_id):
    local_url = cv_db_url + 'api/cvs/' + str(candidate_id)
    try:
        cv, status = consume(local_url)
        return cv
    except HTTPError as e:
        return candidate_id
