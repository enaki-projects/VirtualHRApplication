package org.enaki.license.cv_db.persistence.entities.cv;

import java.sql.Date;

public class ProfessionalExperience {
    public Long professionalExperienceId;
    public Long organisationId;
    public Long uid;
    public String jobName;
    public String description;
    public Date fromDate;
    public Date toDate;

    public ProfessionalExperience(Long professionalExperienceId, Long organisationId, Long uid, String jobName, String description, Date fromDate, Date toDate) {
        this.professionalExperienceId = professionalExperienceId;
        this.organisationId = organisationId;
        this.uid = uid;
        this.jobName = jobName;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

}
