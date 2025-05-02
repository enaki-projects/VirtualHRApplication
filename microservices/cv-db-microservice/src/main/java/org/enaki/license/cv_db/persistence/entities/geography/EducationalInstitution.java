package org.enaki.license.cv_db.persistence.entities.geography;

public class EducationalInstitution {
    public Long educationalInstitutionId;
    public Long countryId;
    public String institutionName;
    public String altInstitutionName;
    public String address;
    public String telephone;
    public String website;
    public String email;
    public String funding;
    public String accreditingAgency;

    public EducationalInstitution(Long educationalInstitutionId, Long countryId, String institutionName, String altInstitutionName, String address, String telephone, String website, String email, String funding, String accreditingAgency) {
        this.educationalInstitutionId = educationalInstitutionId;
        this.countryId = countryId;
        this.institutionName = institutionName;
        this.altInstitutionName = altInstitutionName;
        this.address = address;
        this.telephone = telephone;
        this.website = website;
        this.email = email;
        this.funding = funding;
        this.accreditingAgency = accreditingAgency;
    }
}
