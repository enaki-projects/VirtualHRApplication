import sqlite3
import time

import languages
import locations
import nationalities
import organisations
import roles
import skills
import universities
import users
import cv

DATABASE_PATH = "../../../microservices/cv-db-microservice/cv.db"
DROP_TABLE_SCRIPT_PATH = "../delete_tables.sql"
CREATE_TABLE_SCRIPT_PATH = "../create_tables.sql"


def execute_script(database_path, script_path, message=""):
    start = time.time()
    print("\n*** STARTING SCRIPT FOR {} ***".format(message.upper()))

    conn = sqlite3.connect(database_path)
    cur = conn.cursor()

    try:
        with open(script_path, "r") as script_file:
            cur.executescript(script_file.read())
    except sqlite3.OperationalError as e:
        print(e)
    finally:
        conn.commit()
        conn.close()
        end = time.time()
        print("Finished {} in {} seconds".format(message.upper(), end - start))


def drop_and_create_tables():
    execute_script(database_path=DATABASE_PATH, script_path=DROP_TABLE_SCRIPT_PATH, message="dropping the tables")
    execute_script(database_path=DATABASE_PATH, script_path=CREATE_TABLE_SCRIPT_PATH, message="creating the tables")


def generate_sql_scripts():
    locations.generate_countries_regions_cities()
    skills.generate_skills_sql_scripts(datapath="data/skills.json")
    nationalities.generate_nationalities()
    universities.generate_universities_sql_scripts()
    languages.generate_languages()
    roles.generate_roles_sql_scripts(datapath="data/roles.json")
    users.generate_users_sql_scripts(datapath="data/users.json")


def insert_data_into_tables():
    locations.insert_countries_regions_cities(database_path=DATABASE_PATH)
    skills.insert_skills(database_path=DATABASE_PATH, datapath="data/skills.json")
    nationalities.insert_nationalities(database_path=DATABASE_PATH)
    universities.insert_universities(database_path=DATABASE_PATH)
    languages.insert_languages(database_path=DATABASE_PATH)
    roles.insert_roles(database_path=DATABASE_PATH, datapath="data/roles.json")

    organisations.insert_organisations(database_path=DATABASE_PATH, datapath="data/organisations.json")
    users.insert_users(database_path=DATABASE_PATH, datapath="data/users.json")
    cv.insert_cvs(database_path=DATABASE_PATH, datapath="data/users.json")


def main():
    drop_and_create_tables()
    insert_data_into_tables()
    # generate_sql_scripts()


if __name__ == '__main__':
    main()
