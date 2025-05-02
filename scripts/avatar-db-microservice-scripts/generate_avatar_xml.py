from collections import OrderedDict
from pathlib import Path
import re
from dict2xml import dict2xml


def main():
    Path('output/languages').mkdir(parents=True, exist_ok=True)

    my_dict = OrderedDict({"avatar": {"scene": []}})
    path = Path("../../microservices/avatar-db-microservice/src/main/resources/avatar")
    language_folders = [x for x in path.iterdir() if x.is_dir()]

    for language_folder in language_folders:
        my_language_dict = OrderedDict({"avatar": {"scene": []}})

        print("Current Language Folder to analyze: {}".format(language_folder.name))

        avatar_folders = [x for x in language_folder.iterdir() if x.is_dir()]
        for avatar_folder in avatar_folders:
            print("\tCurrent Folder to analyze: {}".format(avatar_folder.name))
            avatar_subfolders = [x for x in avatar_folder.iterdir() if x.is_dir()]
            for avatar_subfolder in avatar_subfolders:

                print("\t\tCurrent SubFolder to analyze: {}".format(avatar_subfolder.name))
                mp4_files = [file.name for file in list(avatar_subfolder.glob("*.mp4"))]
                dialogue_files = [file.name for file in list(avatar_subfolder.glob("*.txt"))]

                print("\t\t\tMP4 files: {}".format(mp4_files))
                print("\t\t\tTXT files: {}".format(dialogue_files))

                for dialogue_file in dialogue_files:
                    with open("{}/{}".format(str(avatar_subfolder).replace("\\", "/"), dialogue_file), "r") as file:
                        local_scene_dict = OrderedDict({
                            "category": avatar_folder.name,
                            "subcategory": avatar_subfolder.name,
                            "name": dialogue_file[:-4],
                            "language": language_folder.name,
                            "text-to-speech": file.read(),
                            "videos": {"video": []}
                        })

                        for mp4_file in mp4_files:
                            resolution = re.search("[0-9]+x[0-9]+", mp4_file).group(0)
                            local_video_dict = OrderedDict({
                                "resolution": resolution,
                                "path": "{}/{}/{}/{}".format(language_folder.name, avatar_folder.name, avatar_subfolder.name, mp4_file),
                                "video-link": "avatar?name={}&language=en&category={}&subcategory={}&resolution={}".format(dialogue_file[:-4], avatar_folder.name, avatar_subfolder.name, resolution),
                            })

                            local_scene_dict["videos"]["video"].append(local_video_dict.copy())
                        my_dict["avatar"]["scene"].append(local_scene_dict.copy())
                        my_language_dict["avatar"]["scene"].append(local_scene_dict.copy())
        with open("output/languages/{}_avatar.xml".format(language_folder.name), mode="w", encoding="utf-8") as language_file:
            language_file.write("""<?xml version="1.0" encoding="UTF-8"?>\n{}""".format(dict2xml(my_language_dict)))

    print(my_dict)
    with open("output/avatar.xml", mode="w", encoding="utf-8") as output_file:
        output_file.write("""<?xml version="1.0" encoding="UTF-8"?>\n{}""".format(dict2xml(my_dict)))


if __name__ == '__main__':
    main()
