import argparse
import sqlite3
import time

from locations.utils import get_json_from_web, process_locations_data

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(script, tuples, database_path, message="data", method=1):
    start = time.time()
    print("\nInserting {} ...".format(message))

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting {} in {} seconds".format(message, end - start))


def insert_countries_regions_cities(database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING COUNTRIES-REGIONS-CITIES ***")

    countries = get_json_from_web()
    local_countries, local_regions, local_cities = process_locations_data(countries)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from countries, regions and cities ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM cities ")
        cur.execute(" DELETE FROM regions ")
        cur.execute(" DELETE FROM countries ")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    # INSERTING COUNTRIES
    local_countries_tuples = [(country["country_id"], country["name"], country["iso3"], country["iso2"],
                               country["phone_code"], country["capital"], country["currency"])
                              for country in local_countries]
    countries_sql_script = """INSERT OR REPLACE INTO "countries" ("country_id", "country_name", "iso3", "iso2", "phone_code", 
    "capital", "currency") VALUES (?,?,?,?,?,?,?) """
    execute_many(countries_sql_script, local_countries_tuples, database_path, "countries")

    # INSERTING REGIONS
    local_regions_tuples = [(region["region_id"], region["country_id"], region["name"], region["region_code"])
                            for region in local_regions]
    regions_sql_script = """INSERT OR REPLACE INTO "regions" ("region_id", "country_id", "region_name", "region_code") 
    VALUES (?,?,?,?) """
    execute_many(regions_sql_script, local_regions_tuples, database_path, "regions")

    # INSERTING CITIES
    local_cities_tuples = [(city["city_id"], city["region_id"], city["name"], city["latitude"], city["longitude"])
                    for city in local_cities]
    cities_sql_script = """INSERT OR REPLACE INTO "cities" ("city_id", "region_id", "city_name", "latitude", "longitude") 
    VALUES (?,?,?,?,?) """
    execute_many(cities_sql_script, local_cities_tuples, database_path, "cities")


if __name__ == '__main__':
    insert_countries_regions_cities(delete_data=args.delete_data)
