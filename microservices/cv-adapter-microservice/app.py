import os

from dotenv import load_dotenv
from flask import Flask, request
from flask_jwt_extended import JWTManager

from routes import bp, mail
from routes.shared import (
    secret_key, mail_server, mail_port, mail_username, mail_password,
    cors_allow_origin,
)

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

app.register_blueprint(bp, url_prefix="/api")


# CORS. Behind the gateway the browser calls the adapter same-origin, so these
# headers are never consulted (no preflight, no cross-origin). They only matter
# for a genuinely cross-origin caller. To keep credentialed CORS valid we must
# echo a concrete Origin (never '*' together with Allow-Credentials: true), so
# we reflect the request Origin when a wildcard is configured.
@app.after_request
def after_request(response):
    header = response.headers
    origin = request.headers.get('Origin')
    if cors_allow_origin == '*':
        # Reflect the caller's origin so credentialed requests stay valid.
        # Same-origin requests carry no Origin header -> no CORS header added.
        if origin:
            header['Access-Control-Allow-Origin'] = origin
            header['Vary'] = 'Origin'
    else:
        header['Access-Control-Allow-Origin'] = cors_allow_origin
        header['Vary'] = 'Origin'
    header['Access-Control-Allow-Headers'] = 'Content-Type, Authorization'
    header['Access-Control-Allow-Methods'] = 'OPTIONS, HEAD, GET, POST, DELETE, PUT'
    header['Access-Control-Allow-Credentials'] = 'true'
    return response


if __name__ == "__main__":
    # Local development only. In containers the app is served by gunicorn
    # (see Dockerfile): gunicorn -b 0.0.0.0:5000 app:app
    port = int(os.environ.get('PORT', 5000))
    app.run(host='127.0.0.1', debug=True, port=port)
