import json
import time
import urllib
import urllib.request

URL = "https://gist.githubusercontent.com/piraveen/fafd0d984b2236e809d03a0e306c8a4d/raw/4258894f85de7752b78537a4aa66e027090c27ad/languages.json"


def get_json_from_web(url=None):
    if url is None:
        url = URL

    print("\n\tURL: {}".format(url))
    print("\tDownloading raw data from specified url ...")
    start = time.time()
    req = urllib.request.Request(url, headers={'Content-type': 'application/json'})
    response = urllib.request.urlopen(req)
    data_json = json.loads(response.read().decode("utf-8"))

    end = time.time()
    print("\tDownloading data finished in {} seconds".format(end - start))
    return data_json
