import argparse
import sqlite3
import time

from cv.awards import insert_user_awards
from cv.citizenships import insert_user_citizenships
from cv.educational_experiences import insert_user_educational_experience
from cv.hobbies import insert_user_hobbies
from cv.locations import insert_user_locations
from cv.phone_numbers import insert_user_phone_numbers
from cv.professional_experiences import insert_user_professional_experience
from cv.projects import insert_user_projects
from cv.user_languages import insert_user_languages
from cv.user_roles import insert_user_roles
from cv.user_skills import insert_user_skills
from cv.utils import get_cvs

parser = argparse.ArgumentParser()
parser.add_argument('-d', '--delete_data', default=False, action='store_true', dest='delete_data', help='delete data from cities and countries')
args = parser.parse_args()


def execute_many(table_name, cvs, database_path, method=1):
    start = time.time()
    print("\nInserting {} ...".format(table_name))

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    script = """INSERT OR REPLACE INTO "{}" (uid, first_name, last_name, email, birthdate, gender, about_me, motto, image_name, can_take_the_iq_test, requested_retake_of_iq_test) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)""".format(table_name)
    tuples = [(tuple["uid"], tuple["cv"]["first_name"], tuple["cv"]["last_name"], tuple["cv"]["email"], tuple["cv"]["birthdate"], tuple["cv"]["gender"], tuple["cv"]["about_me"], tuple["cv"]["motto"], tuple["cv"]["image_name"], True, False) for tuple in cvs]

    if method == 1:
        cur.executemany(script, tuples)
    else:
        for item in tuples:
            cur.execute(script, item)

    conn.commit()
    conn.close()
    end = time.time()
    print("Finished inserting {} in {} seconds".format(table_name, end - start))


def insert_cvs(datapath="../data/users.json", database_path="../../../../microservices/cv-db-microservice/cv.db", delete_data=False):
    print("\n*** STARTING SCRIPT FOR INSERTING SKILLS ***")
    cvs = get_cvs(datapath)

    ############################################
    if delete_data is True:
        start = time.time()
        print("\nDeleting data from cv ...")

        conn = sqlite3.connect(database_path)
        cur = conn.cursor()

        cur.execute(" DELETE FROM cv")
        conn.commit()
        conn.close()

        end = time.time()
        print("Finished deleting data in {} seconds".format(end - start))

    ############################################
    execute_many(table_name="cv", cvs=cvs, database_path=database_path)

    # Insert User Roles
    insert_user_roles(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Skills
    insert_user_skills(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Awards
    insert_user_awards(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Projects
    insert_user_projects(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Educational Experiences
    insert_user_educational_experience(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Professional Experiences
    insert_user_professional_experience(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Citizenships
    insert_user_citizenships(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Locations
    insert_user_locations(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Hobbies
    insert_user_hobbies(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert Phone Numbers
    insert_user_phone_numbers(database_path=database_path, cvs=cvs, delete_data=delete_data)

    # Insert User Languages
    insert_user_languages(database_path=database_path, cvs=cvs, delete_data=delete_data)


if __name__ == '__main__':
    insert_cvs(delete_data=args.delete_data)