import os

from flask import Blueprint

bp = Blueprint('AdapterMicroservice', __name__)

headers = {'Content-type': 'application/json'}


cv_db_url = "http://localhost:2011/" if os.environ.get('CV_DB_MICROSERVICE') is None \
    else "http://" + os.environ.get('CV_DB_MICROSERVICE') + ":2011/"

jwt_provider_url = "http://localhost:2012/" if os.environ.get('JWT_DB_MICROSERVICE') is None \
    else "http://" + os.environ.get('JWT_DB_MICROSERVICE') + ":2012/"

cv_filter_url = "http://localhost:2013/" if os.environ.get('CV_FILTER_MICROSERVICE') is None \
    else "http://" + os.environ.get('CV_FILTER_MICROSERVICE') + ":2013/"

iq_test_url = "http://localhost:2014/" if os.environ.get('IQ_TEST_MICROSERVICE') is None \
    else "http://" + os.environ.get('IQ_TEST_MICROSERVICE') + ":2014/"

manager_db_url = "http://localhost:2015/" if os.environ.get('MANAGER_DB_MICROSERVICE') is None \
    else "http://" + os.environ.get('MANAGER_DB_MICROSERVICE') + ":2015/"

candidate_url = "http://localhost:3000/" if os.environ.get('CANDIDATE_MICROSERVICE') is None \
    else "http://" + os.environ.get('CANDIDATE_MICROSERVICE') + ":3000/"


if os.environ.get('CV_DB_MICROSERVICE_HEROKU') is not None:
    cv_db_url = os.environ.get('CV_DB_MICROSERVICE_HEROKU')
if os.environ.get('JWT_DB_MICROSERVICE_HEROKU') is not None:
    jwt_provider_url = os.environ.get('JWT_DB_MICROSERVICE_HEROKU')
if os.environ.get('IQ_TEST_MICROSERVICE_HEROKU') is not None:
    iq_test_url = os.environ.get('IQ_TEST_MICROSERVICE_HEROKU')
if os.environ.get('CANDIDATE_MICROSERVICE_HEROKU') is not None:
    candidate_url = os.environ.get('CANDIDATE_MICROSERVICE_HEROKU')

mail_server = "" if os.environ.get('MAIL_SERVER') is None \
    else os.environ.get('MAIL_SERVER')
mail_port = "" if os.environ.get('MAIL_PORT') is None \
    else os.environ.get('MAIL_PORT')
mail_username = "" if os.environ.get('MAIL_USERNAME') is None \
    else os.environ.get('MAIL_USERNAME')
mail_password = "" if os.environ.get('MAIL_PASSWORD') is None \
    else os.environ.get('MAIL_PASSWORD')

secret_key = "" if os.environ.get('SECRET_KEY') is None \
    else os.environ.get('SECRET_KEY')