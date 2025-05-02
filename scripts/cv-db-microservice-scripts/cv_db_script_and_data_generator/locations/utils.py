import json
import time
import urllib
import urllib.request

URL = "https://raw.githubusercontent.com/dr5hn/countries-states-cities-database/master/countries%2Bstates%2Bcities.json"

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


def process_locations_data(countries_to_process):
    try:
        print("\n\tCreating dictionaries from data received")
        start = time.time()

        local_countries = []
        local_regions = []
        local_cities = []

        for country in countries_to_process:
            #if country["iso2"] not in ['MD']:
            #    continue
            local_country = {
                "country_id": country["id"],
                "name": country["name"],
                "iso3": country["iso3"],
                "iso2": country["iso2"],
                "phone_code": country["phone_code"],
                "capital": country["capital"],
                "currency": country["currency"],
            }
            local_countries.append(local_country)
            for region in country["states"]:
                local_region = {
                    "region_id": region["id"],
                    "country_id": country["id"],
                    "name": region["name"],
                    "region_code": region["state_code"]
                }
                local_regions.append(local_region)
                for city in region["cities"]:
                    local_city = {
                        "city_id": city["id"],
                        "region_id": region["id"],
                        "name": city["name"],
                        "latitude": city["latitude"],
                        "longitude": city["longitude"],
                    }
                    local_cities.append(local_city)
        # print(local_countries)
        # print(local_regions)
        # print(local_cities)

        end = time.time()
        print("\tFinished creating dictionaries in {} seconds".format(end - start))
        return local_countries, local_regions, local_cities
    except Exception as generic_exception:
        print(generic_exception)
        print("This functions works only with json from {}".format(URL))