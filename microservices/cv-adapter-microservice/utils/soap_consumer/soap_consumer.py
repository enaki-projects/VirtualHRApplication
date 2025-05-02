import time
import urllib
import urllib.request
from urllib.error import HTTPError

from routes.shared import jwt_provider_url


def soap_consume(data, method='POST', debug=False):
    start, end = None, None
    if debug:
        print("\n************************")
        print("\tSOAP URL: {}".format(jwt_provider_url))
        print("\tFetching raw data from specified url ...")
        start = time.time()
    try:
        req = urllib.request.Request(jwt_provider_url + 'ws', headers={'User-Agent': 'Mozilla/5.0',
                                                                'Content-type': 'text/xml'}, method=method, data=data)

        response = urllib.request.urlopen(req)
        decoded_data = response.read().decode("utf-8")
        if debug:
            end = time.time()
            print("\tFetching data finished in {} seconds".format(end - start))
        return decoded_data, response.status
    except HTTPError as e:
        return e.read().decode("utf-8"), e.code
