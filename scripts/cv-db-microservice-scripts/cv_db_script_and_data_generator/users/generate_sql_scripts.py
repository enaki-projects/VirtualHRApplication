import os
import time

from users.utils import get_users


def insert_data_into_file(users):
    string_builder = ""

    for user in users:
        string_builder += """INSERT INTO "users" ("uid", "email", "password") VALUES ({}, "{}", "{}");\n""" \
            .format(user["uid"], user["email"], user["password"])

    with open("output/insert_users.sql", mode="w", encoding="utf-8") as file:
        file.write(string_builder)


def generate_users_sql_scripts(datapath="../data/users.json"):
    users = get_users(datapath)

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    if not os.path.exists('output'):
        os.makedirs('output')

    insert_data_into_file(users)

    end = time.time()
    print("Finished creating users sql scripts in {} seconds".format(end - start))


if __name__ == '__main__':
    generate_users_sql_scripts()
