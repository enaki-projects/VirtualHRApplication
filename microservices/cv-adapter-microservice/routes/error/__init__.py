from flask import make_response, jsonify

from routes import bp


# -------------------------------------ERROR HANDLING-------------------------------------
@bp.app_errorhandler(400)
def bad_request(msg):
    json_response = {"status": 400, "error": "Invalid Data", "details": msg}
    response = make_response(jsonify(json_response), 400)
    return response


@bp.app_errorhandler(404)
def not_found(error):
    json_response = {"error": "resource not found", "status": 404, "initial_data": str(error)}
    response = make_response(jsonify(json_response), 404)
    return response


@bp.app_errorhandler(405)
def not_allowed(error):
    json_response = {"error": "method not allowed", "status": 405, "initial_data": str(error)}
    response = make_response(jsonify(json_response), 405)
    return response

