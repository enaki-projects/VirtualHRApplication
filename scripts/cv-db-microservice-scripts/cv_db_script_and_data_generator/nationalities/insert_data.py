import argparse
import sqlite3
import time

from nationalities.utils import get_json_from_web

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


def insert_nationalities(database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING NATIONALITIES ***")

    nationalities = get_json_from_web()

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from nationalities ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM nationalities ")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    # INSERTING COUNTRIES
    nationalities_tuples = [(nationality,) for nationality in nationalities]
    nationalities_sql_script = """INSERT OR REPLACE INTO "nationalities" ("nationality") VALUES (?) """
    execute_many(nationalities_sql_script, nationalities_tuples, database_path=database_path, message="nationalities")


if __name__ == '__main__':
    insert_nationalities(delete_data=args.delete_data)
