import io
import json
import time
import urllib
import urllib.request

ROLES_URL = 'http://localhost:2011/api/roles'
LOGIN_URL = 'http://localhost:2011/api/auth/login'
CV_URL = 'http://localhost:2011/api/cvs'


def consume(url=None, debug=False, method='GET', data: dict = None):
    if url is None:
        print('Invalid URL')
        return
    start, end = None, None
    if debug:
        print("\n************************")
        print("\tURL: {}".format(url))
        print("\tFetching raw data from specified url ...")
        start = time.time()
    if data:
        data = json.dumps(data).encode()
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0',
                                               'Content-type': 'application/json'}, method=method, data=data)
    response = urllib.request.urlopen(req)

    decoded_data = response.read().decode("utf-8")
    if debug:
        end = time.time()
        print("\tFetching data finished in {} seconds".format(end - start))
    if len(decoded_data) > 0:
        data_json = json.loads(decoded_data)
        return data_json, response.status
    else:
        return None, response.status


def convert_dict_to_encoded_json(data):
    if not data:
        return None
    return json.dumps(data).encode()


def consume_get_bytes(url=None, debug=True):
    if url is None:
        print('Invalid URL')
        return
    start, end = None, None
    if debug:
        print("\n************************")
        print("\tURL: {}".format(url))
        print("\tFetching raw data from specified url ...")
        start = time.time()
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0',
                                               'Content-type': 'application/json'}, method='GET')
    response = urllib.request.urlopen(req)

    decoded_data = response.read()

    if debug:
        end = time.time()
        print("\tFetching data finished in {} seconds".format(end - start))
    if len(decoded_data) > 0:
        image_bytes = io.BytesIO(decoded_data)
        return (image_bytes, response.headers.get('content-type')), response.status
    else:
        return None, response.status


def consume_update_bytes(url=None, debug=True, data=None, method='PUT', content_type='application/json'):
    if url is None:
        print('Invalid URL')
        return
    start, end = None, None
    if debug:
        print("\n************************")
        print("\tURL: {}".format(url))
        print("\tFetching raw data from specified url ...")
        start = time.time()
    req = urllib.request.Request(url, headers={'User-Agent': 'Mozilla/5.0',
                                               'Content-type': content_type}, method=method, data=data)
    response = urllib.request.urlopen(req)

    decoded_data = response.read().decode("utf-8")
    if debug:
        end = time.time()
        print("\tFetching data finished in {} seconds".format(end - start))
    if len(decoded_data) > 0:
        data_json = json.loads(decoded_data)
        return data_json, response.status
    else:
        return None, response.status


if __name__ == '__main__':
    data = consume(ROLES_URL)
    print(data)
    data = consume(LOGIN_URL, data={
        "email": "eren@gmail.com",
        "passwordHash": "eren"}, method='POST')
    print(data)
