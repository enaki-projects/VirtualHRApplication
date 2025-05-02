package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessEducationalInstitutionBasic {
    public Long educationalInstitutionId;
    public String institutionName;
    public String altInstitutionName;

    public BusinessEducationalInstitutionBasic(Long educationalInstitutionId, String institutionName, String altInstitutionName) {
        this.educationalInstitutionId = educationalInstitutionId;
        this.institutionName = institutionName;
        this.altInstitutionName = altInstitutionName;
    }

    public static BusinessEducationalInstitutionBasic toBusinessEducationalInstitutionBasic(EducationalInstitution educationalInstitution) {
        return new BusinessEducationalInstitutionBasic(
                educationalInstitution.educationalInstitutionId,
                educationalInstitution.institutionName,
                educationalInstitution.altInstitutionName
        );
    }

    public static List<BusinessEducationalInstitutionBasic> toBusinessEducationInstitutionSimplifiedList(List<EducationalInstitution> educationalInstitutionList) {
        return educationalInstitutionList.stream()
                .map(BusinessEducationalInstitutionBasic::toBusinessEducationalInstitutionBasic)
                .collect(Collectors.toList());
    }
}
