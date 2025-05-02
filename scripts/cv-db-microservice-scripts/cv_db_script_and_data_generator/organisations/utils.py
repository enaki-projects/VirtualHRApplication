import json


def get_organisations(datapath):
    try:
        with open(datapath, mode="r") as role_file:
            roles = json.load(role_file)
            return roles
    except Exception as generic_exception:
        print(generic_exception)
        print("Please provide a valid roles json path")