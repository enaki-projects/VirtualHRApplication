package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessEducationalExperience {
    public Long educationalExperienceId;
    public Long educationalInstitutionId;
    public Long uid;
    public String specialty;
    public String qualificationTitle;
    public Date fromDate;
    public Date toDate;
    public HashMap<String, String> references = new HashMap<>();


    public BusinessEducationalExperience(Long educationalExperienceId, Long educationalInstitutionId, Long uid, String specialty, String qualificationTitle, Date fromDate, Date toDate, String serverAddress) {
        this.educationalExperienceId = educationalExperienceId;
        this.educationalInstitutionId = educationalInstitutionId;
        this.uid = uid;
        this.specialty = specialty;
        this.qualificationTitle = qualificationTitle;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.references.put("educationalInstitutionReference", String.format("%s/api/institutions/educational/%s", serverAddress, this.educationalInstitutionId));
    }

    public static BusinessEducationalExperience toBusinessEducationalExperience(EducationalExperience educationalExperience, String serverAddress){
        return new BusinessEducationalExperience(
                educationalExperience.educationalExperienceId,
                educationalExperience.educationalInstitutionId,
                educationalExperience.uid,
                educationalExperience.specialty,
                educationalExperience.qualificationTitle,
                educationalExperience.fromDate,
                educationalExperience.toDate,
                serverAddress
        );
    }

    public static List<BusinessEducationalExperience> toBusinessEducationalExperienceList(List<EducationalExperience> educationalExperienceList, String serverAddress){
        return educationalExperienceList.stream()
                .map(it -> BusinessEducationalExperience.toBusinessEducationalExperience(it, serverAddress))
                .collect(Collectors.toList());
    }
}
