import argparse
import sqlite3
import time

from roles.utils import get_roles

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(roles, database_path, method=1):
    start = time.time()
    print("\nInserting roles")

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    script = """INSERT OR REPLACE INTO "roles" ("role_id", "role_name", "description") VALUES (?, ?, ?)"""
    tuples = [(role["role_id"], role["role_name"], role["description"])for role in roles]

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting roles in {} seconds".format(end - start))


def insert_roles(datapath="../data/roles.json", database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING roleS ***")

    roles = get_roles(datapath)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from interpersonal_roles, digital_roles, organisational_roles and leadership_roles ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM roles")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    ############################################
    execute_many(roles=roles, database_path=database_path)


if __name__ == '__main__':
    insert_roles(delete_data=args.delete_data)
