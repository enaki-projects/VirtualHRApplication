import argparse
import sqlite3
import time

from cv.utils import get_cvs

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(table_name, cv, database_path, method=1):
    start = time.time()
    print("\nInserting {} for uid {}...".format(table_name, cv["uid"]))

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    script = """INSERT OR REPLACE INTO "{}" (uid, skill_id) VALUES (?, ?)""".format(table_name)
    tuples = [(cv["uid"], skill_id) for skill_id in cv[table_name]]

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting {} in {} seconds".format(table_name, end - start))


def insert_user_skills(cvs, database_path="../../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING USER SKILLS ***")

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from skills ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM user_interpersonal_skills")
        cur.execute(" DELETE FROM user_digital_skills ")
        cur.execute(" DELETE FROM user_organisational_skills ")
        cur.execute(" DELETE FROM user_leadership_skills ")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    ############################################
    for cv in cvs:
        if cv.get("user_interpersonal_skills"):
            execute_many(table_name="user_interpersonal_skills", cv=cv, database_path=database_path)
        if cv.get("user_digital_skills"):
            execute_many(table_name="user_digital_skills", cv=cv, database_path=database_path)
        if cv.get("user_organisational_skills"):
            execute_many(table_name="user_organisational_skills", cv=cv, database_path=database_path)
        if cv.get("user_leadership_skills"):
            execute_many(table_name="user_leadership_skills", cv=cv, database_path=database_path)


if __name__ == '__main__':
    datapath = "../../data/users.json"
    cvs = get_cvs(datapath)
    insert_user_skills(cvs, delete_data=args.delete_data)
