package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;

import java.sql.Date;

public class BusinessCreateEducationalExperience {
    public Long educationalInstitutionId;
    public Long uid;
    public String specialty;
    public String qualificationTitle;
    public Date fromDate;
    public Date toDate;

    public BusinessCreateEducationalExperience(Long educationalInstitutionId, Long uid, String specialty, String qualificationTitle, Date fromDate, Date toDate) {
        this.educationalInstitutionId = educationalInstitutionId;
        this.uid = uid;
        this.specialty = specialty;
        this.qualificationTitle = qualificationTitle;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static EducationalExperience toEducationalExperience(BusinessCreateEducationalExperience educationalExperience){
        return new EducationalExperience(
                Integer.toUnsignedLong(0),
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
                qualificationTitle != null &&
                specialty != null &&
                fromDate != null);
    }

    public String getRequiredFieldsMessage(){
        return "Educational Experience require fields (uid, educationalInstitutionId, qualificationTitle, specialty, fromDate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Educational Experience with value fields (educationalInstitutionId, uid, qualificationTitle, specialty, fromDate): (%s, %s, %s, %s, %s) is existent",
                educationalInstitutionId, uid, qualificationTitle, specialty, fromDate);
    }
}
