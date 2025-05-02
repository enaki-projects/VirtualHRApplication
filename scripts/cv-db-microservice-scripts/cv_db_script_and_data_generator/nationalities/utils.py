import json
import time
import urllib
import urllib.request

URL = "https://gist.githubusercontent.com/tiveor/5444753e9919ffe74b41/raw/47e48c7575189ef7ee228e40153a1fa57b5864b1/nationalities.json"


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
