import argparse
import sqlite3
import time

from skills.utils import get_skills

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(skill_name, skills, database_path, method=1):
    start = time.time()
    print("\nInserting {}s ...".format(skill_name))

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    script = """INSERT OR REPLACE INTO "{}s" ("skill_id", "name", "description") VALUES (?, ?, ?)""".format(skill_name)
    tuples = [(skill["skill_id"], skill["name"], skill["description"]) for skill in skills]

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting {}s in {} seconds".format(skill_name, end - start))


def insert_skills(datapath="../data/skills.json", database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING SKILLS ***")

    leadership_skills, organisational_skills, digital_skills, interpersonal_skills = get_skills(datapath)

    # print(leadership_skills)
    # print(organisational_skills)
    # print(digital_skills)
    # print(interpersonal_skills)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from interpersonal_skills, digital_skills, organisational_skills and leadership_skills ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM interpersonal_skills")
        cur.execute(" DELETE FROM digital_skills ")
        cur.execute(" DELETE FROM organisational_skills ")
        cur.execute(" DELETE FROM leadership_skills ")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    ############################################
    execute_many(skill_name="interpersonal_skill", skills=interpersonal_skills, database_path=database_path)
    execute_many(skill_name="digital_skill", skills=digital_skills, database_path=database_path)
    execute_many(skill_name="organisational_skill", skills=organisational_skills, database_path=database_path)
    execute_many(skill_name="leadership_skill", skills=leadership_skills, database_path=database_path)


if __name__ == '__main__':
    insert_skills(delete_data=args.delete_data)
