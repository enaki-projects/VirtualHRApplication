import argparse
import sqlite3
import time

from cv.utils import get_cvs

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(cv, database_path, method=1):
    start = time.time()
    print("\nInserting professional experiences for uid {}".format(cv["uid"]))

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    script = """INSERT OR REPLACE INTO "professional_experiences" (uid, organisation_id, job_name, from_date, to_date) VALUES (?, ?, ?, ?, ?)"""
    tuples = [(cv["uid"], edu["organisation_id"], edu["job_name"], edu["from_date"], edu["to_date"]) for edu in cv["professional_experiences"]]

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting professional experiences in {} seconds".format(end - start))


def insert_user_professional_experience(cvs, database_path="../../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING PROFESSIONAL EXPERIENCES ***")
    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from professional experiences ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM professional_experiences")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    ############################################
    for cv in cvs:
        if cv.get("professional_experiences"):
            execute_many(cv=cv, database_path=database_path)


if __name__ == '__main__':
    datapath = "../../data/users.json"
    cvs = get_cvs(datapath)
    insert_user_professional_experience(cvs, delete_data=args.delete_data)
