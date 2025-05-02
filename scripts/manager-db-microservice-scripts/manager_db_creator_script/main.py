import time
import sqlite3

DATABASE_PATH = "../../../microservices/manager-db-microservice/manager.db"
DROP_TABLE_SCRIPT_PATH = "../manager_db_delete_tables.sql"
CREATE_TABLE_SCRIPT_PATH = "../manager_db_create_tables.sql"


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


def main():
    drop_and_create_tables()


if __name__ == '__main__':
    main()