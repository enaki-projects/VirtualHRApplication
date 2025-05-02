import argparse
import sqlite3
import time

from universities.utils import get_json_from_web

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(script, tuples, database_path, method=1, table_name="educational_institution"):
    start = time.time()
    print("\nInserting {}s ...".format(table_name))

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
    print("Finished inserting {}s in {} seconds".format(table_name, end - start))


def insert_universities(database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING UNIVERSITIES ***")

    universities = get_json_from_web()
    # print(universities)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from educational institutions ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM educational_institutions")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    script = """INSERT OR REPLACE INTO "educational_institutions" ("country_id", "institution_name", "alt_institution_name", "address", "telephone", "website", "email", "funding", "accrediting_agency") VALUES ((SELECT country_id from countries where country_name like ?), ?, ?, ?, ?, ?, ?, ?, ?)"""

    tuples = []
    for university in universities:
        address = "{}, {}".format(university["address"]["city"], university["country"]) \
            if university["address"]["street"] is None or university["address"]["street"]=="None" \
            else "{}, {}, {}".format(university["address"]["street"].replace("\"", "'"), university["address"]["city"], university["country"])
        contacts = university["contact"]
        institution_name = university["name"]
        alt_institution_name = university["alt_name"]
        telephone = contacts["telephone"]
        website = contacts["website"]
        email = contacts["email"]
        funding = university["funding"]
        accrediting_agency = university["accrediting_agency"]
        country = university["country"]
        if country == "DR Congo":
            country = "Congo The Democratic Republic Of The"
        elif country == "North Korea":
            country = "Korea North"
        elif country == "South Korea":
            country = "Korea South"
        elif country == "Palestine":
            country = "Palestinian Territory Occupied"
        elif country == "Republic of the Congo":
            country = "Congo"
        elif country == "Timor-Leste":
            country = "East Timor"

        like_country = '%'+country+'%'
        tuples.append((like_country, institution_name, alt_institution_name, address, telephone, website, email, funding, accrediting_agency))

    ############################################
    execute_many(script=script, tuples=tuples, database_path=database_path)


if __name__ == '__main__':
    insert_universities(delete_data=args.delete_data)
