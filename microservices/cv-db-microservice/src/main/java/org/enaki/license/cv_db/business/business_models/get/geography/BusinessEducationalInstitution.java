package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessEducationalInstitution {
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
    public HashMap<String, String> references = new HashMap<>();

    public BusinessEducationalInstitution(Long educationalInstitutionId, Long countryId, String institutionName, String altInstitutionName, String address, String telephone, String website, String email, String funding, String accreditingAgency, String serverAddress) {
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
        this.references.put("countryReference", String.format("%s/api/geography/countries/%s", serverAddress, this.countryId));
    }

    public static BusinessEducationalInstitution toBusinessEducationalInstitution(EducationalInstitution educationalInstitution, String serverAddress) {
        return new BusinessEducationalInstitution(
                educationalInstitution.educationalInstitutionId,
                educationalInstitution.countryId,
                educationalInstitution.institutionName,
                educationalInstitution.altInstitutionName,
                educationalInstitution.address,
                educationalInstitution.telephone,
                educationalInstitution.website,
                educationalInstitution.email,
                educationalInstitution.funding,
                educationalInstitution.accreditingAgency,
                serverAddress
        );
    }

    public static List<BusinessEducationalInstitution> toBusinessEducationInstitutionList(List<EducationalInstitution> educationalInstitutionList, String serverAddress) {
        return educationalInstitutionList.stream()
                .map(it -> BusinessEducationalInstitution.toBusinessEducationalInstitution(it, serverAddress))
                .collect(Collectors.toList());
    }
}
