import json
import os
import os.path
from os import path


def add_action(token, data, action):
    with open("workdir/{}_{}.txt".format(action, token), mode="w") as file:
        json.dump(data, file)


def read_action(token, action):
    with open("workdir/{}_{}.txt".format(action, token), mode="r") as file:
        data = json.load(file)
        return data


def delete_action(token, action):
    if exist_action(token, action):
        os.remove("workdir/{}_{}.txt".format(action, token))


def exist_action(token, action):
    return path.exists("workdir/{}_{}.txt".format(action, token))
