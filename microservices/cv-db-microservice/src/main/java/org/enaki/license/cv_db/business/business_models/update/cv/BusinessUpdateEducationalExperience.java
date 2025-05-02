package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateEducationalExperience;
import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;

import java.sql.Date;

public class BusinessUpdateEducationalExperience {
    public Long educationalInstitutionId;
    public Long uid;
    public String specialty;
    public String qualificationTitle;
    public Date fromDate;
    public Date toDate;

    public BusinessUpdateEducationalExperience(Long educationalInstitutionId, Long uid, String specialty, String qualificationTitle, Date fromDate, Date toDate) {
        this.educationalInstitutionId = educationalInstitutionId;
        this.uid = uid;
        this.specialty = specialty;
        this.qualificationTitle = qualificationTitle;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static EducationalExperience toEducationalExperience(Long educationalExperienceId, BusinessUpdateEducationalExperience educationalExperience) {
        return new EducationalExperience(
                educationalExperienceId,
                educationalExperience.educationalInstitutionId,
                educationalExperience.uid,
                educationalExperience.specialty,
                educationalExperience.qualificationTitle,
                educationalExperience.fromDate,
                educationalExperience.toDate
        );
    }

    public static BusinessCreateEducationalExperience toBusinessCreateEducationalExperience(BusinessUpdateEducationalExperience educationalExperience) {
        return new BusinessCreateEducationalExperience(
                educationalExperience.educationalInstitutionId,
                educationalExperience.uid,
                educationalExperience.specialty,
                educationalExperience.qualificationTitle,
                educationalExperience.fromDate,
                educationalExperience.toDate
        );
    }

    public boolean isValid() {
        return (educationalInstitutionId != null &&
                uid != null &&
                specialty != null &&
                qualificationTitle != null &&
                fromDate != null);
    }

    public String getRequiredFieldsMessage(){
        return "Educational Experience require fields (educationalInstitutionId, uid, qualificationTitle, specialty, fromDate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Educational Experience with value fields (educationalInstitutionId, uid, qualificationTitle, specialty, fromDate): (%s, %s, %s, %s, %s) is existent",
                educationalInstitutionId, uid, qualificationTitle, specialty, fromDate);
    }
}
