from urllib.error import HTTPError

from flask import Response, request, abort
from flask_jwt_extended import jwt_required

from routes import bp, manager_db_url, headers, consume, convert_dict_to_encoded_json, restricted


# -------------------------------------cart_items ROUTES-------------------------------------
@bp.route('/manager/cart-items', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_items():
    current_url = manager_db_url + 'api/cart-items'
    try:
        cart_items, status = consume(current_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cart_items), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/<uid>/cart-items', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_items_by_uid(uid):
    if uid is None:
        return abort(400, "Uid not specified")
    local_url = manager_db_url + 'api/' + uid + '/cart_items'
    try:
        cart_items, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cart_items), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-items/<id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_item_by_id(id):
    local_url = manager_db_url + 'api/cart-items/' + id
    try:
        cart_item, status = consume(local_url, debug=False)
        return Response(response=convert_dict_to_encoded_json(cart_item), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-items/by-cart/<id>', methods=['GET'])
@jwt_required()
@restricted(access_level="manager")
def get_cart_items_by_cart(id):
    cart_data = request.json
    local_url = manager_db_url + 'api/cart-items/by-cart/' + id
    try:
        response_data, status = consume(local_url, method='GET', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-items', methods=['POST'])
@jwt_required()
@restricted(access_level="manager")
def create_cart_item():
    cart_data = request.json
    local_url = manager_db_url + 'api/cart-items'
    try:
        response_data, status = consume(local_url, method='POST', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-items/<id>', methods=['PUT'])
@jwt_required()
@restricted(access_level="manager")
def update_cart_item(id):
    cart_data = request.json
    local_url = manager_db_url + 'api/cart-items/' + id
    try:
        response_data, status = consume(local_url, method='PUT', data=cart_data, debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())


@bp.route('/manager/cart-items/<id>', methods=['DELETE'])
@jwt_required()
@restricted(access_level="manager")
def delete_cart_item(id):
    local_url = manager_db_url + 'api/cart-items/' + id
    try:
        response_data, status = consume(local_url, method='DELETE', debug=False)
        return Response(response=convert_dict_to_encoded_json(response_data), headers=headers, status=status)
    except HTTPError as e:
        return Response(status=e.code, headers=headers, response=e.read())