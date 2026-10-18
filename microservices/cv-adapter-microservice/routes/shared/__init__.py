import logging
import os

from flask import Blueprint

logger = logging.getLogger(__name__)

bp = Blueprint('AdapterMicroservice', __name__)

headers = {'Content-type': 'application/json'}


def _resolve_url(full_url_env, host_env, port, default_host='localhost'):
    """Resolve a downstream base URL.

    Precedence:
      1. explicit full URL env var (e.g. CV_DB_URL=http://cv-db:2011/)
      2. legacy host-name env var (e.g. CV_DB_MICROSERVICE=cv-db) -> http://<host>:<port>/
      3. http://localhost:<port>/
    A trailing slash is always guaranteed (existing code concatenates "api/...").
    """
    full = os.environ.get(full_url_env)
    if full:
        return full if full.endswith('/') else full + '/'
    host = os.environ.get(host_env)
    if host:
        return "http://{}:{}/".format(host, port)
    return "http://{}:{}/".format(default_host, port)


cv_db_url = _resolve_url('CV_DB_URL', 'CV_DB_MICROSERVICE', 2011)
jwt_provider_url = _resolve_url('JWT_PROVIDER_URL', 'JWT_DB_MICROSERVICE', 2012)
cv_filter_url = _resolve_url('CV_FILTER_URL', 'CV_FILTER_MICROSERVICE', 2013)
iq_test_url = _resolve_url('IQ_TEST_URL', 'IQ_TEST_MICROSERVICE', 2014)
manager_db_url = _resolve_url('MANAGER_DB_URL', 'MANAGER_DB_MICROSERVICE', 2015)
# The candidate (user) frontend is referenced in recovery emails. Behind the
# gateway it is same-origin root "/", but a full external URL can be supplied.
candidate_url = _resolve_url('CANDIDATE_URL', 'CANDIDATE_MICROSERVICE', 3000)

# Legacy Heroku full-URL overrides (kept for backwards compatibility).
if os.environ.get('CV_DB_MICROSERVICE_HEROKU') is not None:
    cv_db_url = os.environ.get('CV_DB_MICROSERVICE_HEROKU')
if os.environ.get('JWT_DB_MICROSERVICE_HEROKU') is not None:
    jwt_provider_url = os.environ.get('JWT_DB_MICROSERVICE_HEROKU')
if os.environ.get('IQ_TEST_MICROSERVICE_HEROKU') is not None:
    iq_test_url = os.environ.get('IQ_TEST_MICROSERVICE_HEROKU')
if os.environ.get('CANDIDATE_MICROSERVICE_HEROKU') is not None:
    candidate_url = os.environ.get('CANDIDATE_MICROSERVICE_HEROKU')

mail_server = os.environ.get('MAIL_SERVER', '')
mail_port = os.environ.get('MAIL_PORT', '')
mail_username = os.environ.get('MAIL_USERNAME', '')
mail_password = os.environ.get('MAIL_PASSWORD', '')

# JWT verification secret. MUST match jwt-provider's signing secret (raw UTF-8
# bytes, HMAC-SHA256/384/512). No secret is baked into the image; a fake dev
# fallback is used only when SECRET_KEY is unset, and a warning is logged.
secret_key = os.environ.get('SECRET_KEY')
if not secret_key:
    secret_key = 'dev-only-change-me'
    logger.warning(
        "SECRET_KEY not set; using insecure dev fallback 'dev-only-change-me'. "
        "JWT verification will FAIL against jwt-provider unless both share the real secret."
    )

# Allowed CORS origin. '*' for dev; set CORS_ALLOW_ORIGIN to the public origin
# in production. When same-origin behind the gateway, CORS headers are harmless.
cors_allow_origin = os.environ.get('CORS_ALLOW_ORIGIN', '*')
