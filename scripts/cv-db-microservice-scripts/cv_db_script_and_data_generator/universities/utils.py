import json
import os
import time
import zipfile

import requests

URL = "https://github.com/rinvex/universities/archive/master.zip"


def download_url(url, save_path, chunk_size=128):
    r = requests.get(url, stream=True)
    with open(save_path, 'wb') as fd:
        for chunk in r.iter_content(chunk_size=chunk_size):
            fd.write(chunk)


def get_json_from_web(url=None):
    if url is None:
        url = URL

    print("\n\tURL: {}".format(url))
    print("\tDownloading raw data from specified url ...")
    start = time.time()

    if os.path.exists("resources.zip"):
        os.remove("resources.zip")

    download_url(URL, "resources.zip")

    universities = []

    with zipfile.ZipFile("resources.zip") as zf:
        for name in zf.namelist():
            if ".json" in name and "composer.json" not in name and 'names.json' not in name:
                try:
                    my_json = zf.read(name).decode('utf-8')
                    my_dict = json.loads(my_json)
                    universities.append(my_dict)
                except json.decoder.JSONDecodeError as e:
                    print("\t\terror processing {} : <{}>".format(name, e))
    universities = sorted(universities, key=lambda k: k['country'])

    if os.path.exists("resources.zip"):
        os.remove("resources.zip")

    end = time.time()
    print("\tDownloading data finished in {} seconds".format(end - start))
    return universities


if __name__ == '__main__':
    get_json_from_web()
