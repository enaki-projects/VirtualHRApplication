import json


def get_cvs(datapath):
    try:
        with open(datapath, mode="r") as user_file:
            users = json.load(user_file)
            return users
    except Exception as generic_exception:
        print(generic_exception)
        print("Please provide a valid users json path")
