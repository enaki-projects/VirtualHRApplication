import os
import time

from languages.utils import get_json_from_web


def generate_languages():
    languages = get_json_from_web()

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    nationality_string_builder = ""

    for code, language in languages.items():
        nationality_string_builder += """INSERT OR REPLACE INTO "languages" ("name", "code", "native_name") VALUES ("{}", "{}", "{}");\n"""\
            .format(language["name"], code, language["nativeName"])

    if not os.path.exists('output'):
        os.makedirs('output')

    with open("output/insert_nationality.sql", mode="w", encoding="utf-8") as file:
        file.write(nationality_string_builder)

    end = time.time()
    print("Finished creating scripts nationality.sql in {} seconds".format(end - start))


if __name__ == '__main__':
    generate_languages()
