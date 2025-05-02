import json


def get_skills(datapath):
    try:
        with open(datapath, mode="r") as skill_file:
            skills = json.load(skill_file)
            return skills["leadership_skills"], skills["organisational_skills"], skills["digital_skills"], skills["interpersonal_skills"]
    except Exception as generic_exception:
        print(generic_exception)
        print("Please provide a valid skills json path")