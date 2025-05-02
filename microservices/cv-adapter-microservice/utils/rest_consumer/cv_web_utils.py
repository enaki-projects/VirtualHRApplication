from utils.rest_consumer import consume


def process_cv(cv):
    del cv["cvReferences"]["serverAddress"]
    for key in cv["cvReferences"].keys():
        element_list = []
        if cv["cvReferences"][key]:
            for current_url in cv["cvReferences"][key]:
                element_data, _ = consume(current_url)
                if 'uid' in element_data:
                    del element_data["uid"]
                element_list.append(element_data)
            if key == "phoneNumbers":
                for element in element_list:
                    process_phone_number(element)
            if key == "locations":
                for element in element_list:
                    process_location(element)
            if key == "professionalExperiences":
                for element in element_list:
                    process_professional_experience(element)
            if key == "educationalExperiences":
                for element in element_list:
                    process_educational_experience(element)
            if key == "userLanguages":
                for element in element_list:
                    process_user_language(element)
            cv["cvReferences"][key] = element_list
    return cv


def process_phone_number(raw_phone_number):
    element_data, _ = consume(raw_phone_number["references"]["countryReference"])
    raw_phone_number["country"] = element_data
    del raw_phone_number["countryId"]
    del raw_phone_number["references"]
    return raw_phone_number


def process_region(raw_region):
    element_data, _ = consume(raw_region["references"]["countryReference"])
    raw_region["country"] = element_data
    del raw_region["countryId"]
    del raw_region["references"]
    return raw_region


def process_city(raw_city):
    element_data, _ = consume(raw_city["references"]["regionReference"])
    raw_city["region"] = process_region(element_data)
    del raw_city["regionId"]
    del raw_city["references"]
    return raw_city


def process_location(raw_location):
    element_data, _ = consume(raw_location["references"]["cityReference"])
    raw_location["city"] = process_city(element_data)
    del raw_location["cityId"]
    del raw_location["references"]
    return raw_location


def process_educational_experience(raw_educational_experience):
    element_data, _ = consume(raw_educational_experience["references"]["educationalInstitutionReference"])
    raw_educational_experience["educationalInstitution"] = process_educational_institution(element_data)
    del raw_educational_experience["educationalInstitutionId"]
    del raw_educational_experience["references"]
    return raw_educational_experience


def process_educational_institution(raw_educational_institution):
    element_data, _ = consume(raw_educational_institution["references"]["countryReference"])
    raw_educational_institution["country"] = element_data
    del raw_educational_institution["countryId"]
    del raw_educational_institution["references"]
    return raw_educational_institution


def process_professional_experience(raw_professional_experience):
    element_data, _ = consume(raw_professional_experience["references"]["organisationReference"])
    raw_professional_experience["organisation"] = process_organisation(element_data)
    del raw_professional_experience["organisationId"]
    del raw_professional_experience["references"]
    return raw_professional_experience


def process_organisation(raw_organisation):
    element_data, _ = consume(raw_organisation["references"]["cityReference"])
    raw_organisation["city"] = process_city(element_data)
    del raw_organisation["cityId"]
    del raw_organisation["references"]
    return raw_organisation


def process_user_language(raw_user_language):
    element_data, _ = consume(raw_user_language["references"]["languageReference"])
    raw_user_language["language"] = element_data
    del raw_user_language["languageId"]
    del raw_user_language["references"]
    return raw_user_language
