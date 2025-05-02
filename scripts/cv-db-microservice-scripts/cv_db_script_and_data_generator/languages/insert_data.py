import argparse
import sqlite3
import time

from languages.utils import get_json_from_web

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


def insert_languages(database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING LANGUAGES ***")

    languages = get_json_from_web()

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from languages ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM languages ")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    # INSERTING COUNTRIES
    languages_tuples = [(language["name"], code, language["nativeName"]) for code, language in languages.items()]
    languages_sql_script = """INSERT OR REPLACE INTO "languages" ("name", "code", "native_name") VALUES (?, ?, NULLIF(?, '')) """
    execute_many(languages_sql_script, languages_tuples, database_path=database_path, message="languages")


if __name__ == '__main__':
    insert_languages(delete_data=args.delete_data)
