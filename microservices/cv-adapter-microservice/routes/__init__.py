from utils import *
from routes.shared import bp, cv_db_url, headers, iq_test_url, manager_db_url
from routes.error import *
from routes.auth import *
from routes.cv import *
from routes.geography import *
from routes.institutions import *
from routes.iq_tests import *
from routes.manager import *
from routes.users import *


@bp.route('/', methods=['GET'])
def hello_world():
    return jsonify({'message': 'Hello, World! from {}'.format(cv_db_url)})


@bp.after_request # blueprint can also be app~~
def after_request(response):
    header = response.headers
    header['Access-Control-Allow-Origin'] = '*'
    return response