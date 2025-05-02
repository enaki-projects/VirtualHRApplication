import os
import time

from roles.utils import get_roles


def insert_data_into_file(roles):
    string_builder = ""

    for role in roles:
        string_builder += """INSERT INTO "roles" ("role_id", "role_name", "description") VALUES ({}, "{}", "{}");\n""" \
            .format(role["role_id"], role["role_name"], role["description"])

    with open("output/insert_roles.sql", mode="w", encoding="utf-8") as file:
        file.write(string_builder)


def generate_roles_sql_scripts(datapath="../data/roles.json"):
    roles = get_roles(datapath)

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    if not os.path.exists('output'):
        os.makedirs('output')

    insert_data_into_file(roles)

    end = time.time()
    print("Finished creating roles sql scripts in {} seconds".format(end - start))


if __name__ == '__main__':
    generate_roles_sql_scripts()
