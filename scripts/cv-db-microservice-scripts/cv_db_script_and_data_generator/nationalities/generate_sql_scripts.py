import os
import time

from nationalities.utils import get_json_from_web


def generate_nationalities():
    nationalities = get_json_from_web()

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    nationality_string_builder = ""

    for nationality in nationalities:
        nationality_string_builder += """INSERT INTO "nationalities" ("nationality") VALUES ("{}");\n""".format(nationality)

    if not os.path.exists('output'):
        os.makedirs('output')

    with open("output/insert_nationality.sql", mode="w", encoding="utf-8") as file:
        file.write(nationality_string_builder)

    end = time.time()
    print("Finished creating scripts nationality.sql in {} seconds".format(end - start))


if __name__ == '__main__':
    generate_nationalities()
