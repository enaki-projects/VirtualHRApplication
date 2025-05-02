package org.enaki.license.cv_db.persistence.entities.cv;

import java.sql.Date;

public class EducationalExperience {
    public Long educationalExperienceId;
    public Long educationalInstitutionId;
    public Long uid;
    public String qualificationTitle;
    public String specialty;
    public Date fromDate;
    public Date toDate;

    public EducationalExperience(Long educationalExperienceId, Long educationalInstitutionId, Long uid, String specialty, String qualificationTitle, Date fromDate, Date toDate) {
        this.educationalExperienceId = educationalExperienceId;
        this.educationalInstitutionId = educationalInstitutionId;
        this.uid = uid;
        this.qualificationTitle = qualificationTitle;
        this.specialty = specialty;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

}
