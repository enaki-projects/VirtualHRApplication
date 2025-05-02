import argparse
import sqlite3
import time

from users.utils import get_users
import hashlib
import uuid

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def hash_pass(text):
    salt = "{" + uuid.uuid4().hex + "}"
    return salt + hashlib.sha256(text.encode() + salt.encode()).hexdigest()


def execute_many(users, database_path, method=1):
    start = time.time()
    print("\nInserting users")

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    script = """INSERT OR REPLACE INTO "users" ("uid", "email", "password") VALUES (?, ?, ?)"""
    tuples = [(user["uid"], user["email"], '{SHA-256}' + hash_pass(user["password"])) for user in users]

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting users in {} seconds".format(end - start))


def insert_users(datapath="../data/users.json", database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING userS ***")

    users = get_users(datapath)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from users ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM users")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    ############################################
    execute_many(users=users, database_path=database_path)


if __name__ == '__main__':
    insert_users(delete_data=args.delete_data)
