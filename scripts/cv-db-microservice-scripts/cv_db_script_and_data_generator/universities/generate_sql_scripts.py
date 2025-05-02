import os
import time

from universities.utils import get_json_from_web


def assign_value(value):
    if value is None:
        return "Null"
    #value = value.replace("\"'", "\"").replace("'\"", "\"")
    if value != "None":
        return "\"{}\"".format(value.replace("\"", "'"))
    return "Null"


def insert_data_into_file(universities, table_name="educational_institution"):
    string_builder = ""
    id = 0
    for university in universities:
        if university["address"]["street"] is None:
            address = "\"{}, {}\"".format(university["address"]["city"], university["country"])
        else:
            address = "\"{}, {}, {}\"".format(university["address"]["street"].replace("\"", "'"),
                                              university["address"]["city"], university["country"])
        contacts = university["contact"]
        institution_name = assign_value(university["name"])
        alt_institution_name = assign_value(university["alt_name"])
        telephone = assign_value(contacts["telephone"])
        website = assign_value(contacts["website"])
        email = assign_value(contacts["email"])
        funding = assign_value(university["funding"])
        accrediting_agency = assign_value(university["accrediting_agency"])

        id += 1

        string_builder += """INSERT INTO "{}s" ("{}_id", "country_id", "institution_name", "alt_institution_name", "address", "telephone", "website", "email", "funding", "accrediting_agency") VALUES ({}, (SELECT country_id from countries where name="{}"), {}, {}, {}, {}, {}, {}, {}, {});\n"""\
            .format(table_name, table_name, id, university["country"], institution_name, alt_institution_name, address,
                    telephone, website, email, funding,  accrediting_agency)

    with open("output/insert_{}s.sql".format(table_name), mode="w", encoding="utf-8") as file:
        file.write(string_builder)


def generate_universities_sql_scripts():
    universities = get_json_from_web()
    # print(universities)

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    if not os.path.exists('output'):
        os.makedirs('output')

    insert_data_into_file(universities)

    end = time.time()
    print("Finished creating universities sql scripts in {} seconds".format(end - start))


if __name__ == '__main__':
    generate_universities_sql_scripts()
