import os
import time

from locations.utils import get_json_from_web, process_locations_data


def generate_countries_regions_cities():
    countries = get_json_from_web()
    local_countries, local_regions, local_cities = process_locations_data(countries)

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    country_string_builder = ""
    region_string_builder = ""
    city_string_builder = ""

    for country in local_countries:
        country_string_builder += """INSERT INTO "countries" ("country_id", "country_name", "iso3", "iso2", "phone_code", "capital", "currency") VALUES ("{}", "{}", "{}", "{}", "{}", "{}", "{}");\n""".format(country["country_id"], country["name"],
                                                                     country["iso3"], country["iso2"]
                                                                     , country["phone_code"], country["capital"],
                                                                     country["currency"])

    for region in local_regions:
        region_string_builder += """INSERT INTO "regions" ("region_id", "country_id", "region_name", "region_code") VALUES ("{}", "{}", "{}", "{}");\n""".format(
            region["region_id"], region["country_id"],
            region["name"], region["region_code"])

    for city in local_cities:
        city_string_builder += """INSERT INTO "cities" ("city_id", "region_id", "city_name", "latitude", "longitude") VALUES ("{}", "{}", "{}", "{}", "{}");\n""".format(
            city["city_id"], city["region_id"],
            city["name"], city["latitude"], city["longitude"])

    if not os.path.exists('output'):
        os.makedirs('output')

    with open("output/insert_country.sql", mode="w", encoding="utf-8") as country_file:
        country_file.write(country_string_builder)

    with open("output/insert_region.sql", mode="w", encoding="utf-8") as region_file:
        region_file.write(region_string_builder)

    with open("output/insert_city.sql", mode="w", encoding="utf-8") as city_file:
        city_file.write(city_string_builder)

    end = time.time()
    print("Finished creating scripts [insert_city.sql, insert_country.sql and insert_region.sql] in {} seconds".format(
        end - start))


if __name__ == '__main__':
    generate_countries_regions_cities()
