from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, manager_db_url, headers, consume, convert_dict_to_encoded_json, restricted


# -------------------------------------cart_maintainers ROUTES-------------------------------------
@bp.route('/manager/cart-maintainers', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_maintainers():
    current_url = manager_db_url + 'api/cart-maintainers'
    try:
        cart_maintainers, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cart_maintainers), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/<uid>/cart-maintainers', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_maintainers_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = manager_db_url + 'api/' + uid + '/cart_maintainers'
    try:
        cart_maintainers, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cart_maintainers), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-maintainers/<id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_maintainer_by_id(id):
    local_url = manager_db_url + 'api/cart-maintainers/' + id
    try:
        cart_maintainer, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cart_maintainer), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-maintainers/by-cart/<id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_maintainers_by_cart(id):
    cart_data = request.json
    local_url = manager_db_url + 'api/cart-maintainers/by-cart/' + id
    try:
        response_data, status = consume(local_url, method='GET', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-maintainers', methods=['POST'])
@jwt_required()
@restricted(access_level="manager")
def create_cart_maintainer():
    cart_data = request.json
    local_url = manager_db_url + 'api/cart-maintainers'
    try:
        response_data, status = consume(local_url, method='POST', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-maintainers/<id>', methods=['PUT'])
@jwt_required()
@restricted(access_level="manager")
def update_cart_maintainer(id):
    cart_data = request.json
    local_url = manager_db_url + 'api/cart-maintainers/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-maintainers/<id>', methods=['DELETE'])
@jwt_required()
@restricted(access_level="manager")
def delete_cart_maintainer(id):
    local_url = manager_db_url + 'api/cart-maintainers/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())