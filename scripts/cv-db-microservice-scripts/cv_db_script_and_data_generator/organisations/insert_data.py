import argparse
import sqlite3
import time

from nationalities.utils import get_json_from_web
from organisations.utils import get_organisations

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


def insert_organisations(database_path="../../../../microservices/cv-db-microservice/cv.db", datapath="../data/organisations.json", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING ORGANISATIONS ***")

    organisations = get_organisations(datapath)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from organisations ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM organisations ")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    # INSERTING COUNTRIES
    organisations_tuples = [(organisation["city_id"], organisation["organisation_name"], organisation["address"]) for organisation in organisations]
    organisations_sql_script = """INSERT OR REPLACE INTO "organisations" ("city_id", "organisation_name", "address") VALUES (?, ?, ?) """
    execute_many(organisations_sql_script, organisations_tuples, database_path=database_path, message="organisations")


if __name__ == '__main__':
    insert_organisations(delete_data=args.delete_data)
