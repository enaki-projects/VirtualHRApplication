import os

from dotenv import load_dotenv
from flask import Flask
from flask_jwt_extended import JWTManager

from routes import bp, mail
from routes.shared import secret_key, mail_server, mail_port, mail_username, mail_password

load_dotenv()

app = Flask(__name__)

# configure jwt
app.config['JWT_SECRET_KEY'] = secret_key
app.config['JWT_TOKEN_LOCATION'] = ['cookies', 'headers']
app.config['JWT_DECODE_ALGORITHMS'] = ['HS256', 'HS384', 'HS512']

app.config['MAIL_SERVER'] = mail_server
app.config['MAIL_PORT'] = mail_port
app.config['MAIL_USERNAME'] = mail_username
app.config['MAIL_PASSWORD'] = mail_password
app.config['MAIL_USE_TLS'] = True
app.config['MAIL_USE_SSL'] = False

mail.init_app(app)

jwt = JWTManager(app)

# app.config['APPLICATION_ROOT'] = '/api'
app.register_blueprint(bp, url_prefix="/api")


# CORS
@app.after_request
def after_request(response):
    header = response.headers
    header['Access-Control-Allow-Origin'] = '*'
    header['Access-Control-Allow-Headers'] = 'Content-Type, Authorization'
    header['Access-Control-Allow-Methods'] = 'OPTIONS, HEAD, GET, POST, DELETE, PUT'
    header['Access-Control-Allow-Credentials'] = 'true'
    # header['Access-Control-Allow-Private-Network'] = 'true'
    return response


if __name__ == "__main__":
    port = int(os.environ.get('PORT', 5000))
    #app.run(host='0.0.0.0', debug=True, port=port)
    #app.run(host='0.0.0.0', debug=True, port=port, ssl_context='adhoc')
    app.run(host='127.0.0.1', debug=True, port=port, ssl_context='adhoc')
