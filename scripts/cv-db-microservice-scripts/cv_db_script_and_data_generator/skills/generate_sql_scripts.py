import os
import time

from skills.utils import get_skills


def insert_data_into_file(skills, skill_name):
    string_builder = ""

    for skill in skills:
        string_builder += """INSERT INTO "{}s" ("skill_id", "name", "description") VALUES ("{}", "{}", "{}");\n""" \
            .format(skill_name, skill["skill_id"], skill["name"], skill["description"])

    with open("output/insert_{}s.sql".format(skill_name), mode="w", encoding="utf-8") as file:
        file.write(string_builder)


def generate_skills_sql_scripts(datapath="../data/skills.json"):
    leadership_skills, organisational_skills, digital_skills, interpersonal_skills = get_skills(datapath)

    # print(leadership_skills)
    # print(organisational_skills)
    # print(digital_skills)
    # print(interpersonal_skills)

    ############################################
    start = time.time()
    print("\nInserting Data into files...........")

    if not os.path.exists('output'):
        os.makedirs('output')

    insert_data_into_file(leadership_skills, skill_name="leadership_skill")
    insert_data_into_file(organisational_skills, skill_name="organisational_skill")
    insert_data_into_file(digital_skills, skill_name="digital_skill")
    insert_data_into_file(interpersonal_skills, skill_name="interpersonal_skill")

    end = time.time()
    print("Finished creating skills sql scripts in {} seconds".format(end - start))


if __name__ == '__main__':
    generate_skills_sql_scripts()
