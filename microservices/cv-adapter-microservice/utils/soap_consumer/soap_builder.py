def read_soap_template(path):
    with open(path) as soap_template_file:
        return soap_template_file.read()

auth_template = read_soap_template("utils/soap_templates/soap_auth_template.xml")
jwt_decoder_template = read_soap_template("utils/soap_templates/soap_auth_template.xml")

get_soap_auth_envelope = lambda email, password_hash: auth_template.format(email, password_hash)
get_soap_jwt_envelope = lambda jwt: jwt_decoder_template.format(jwt)


