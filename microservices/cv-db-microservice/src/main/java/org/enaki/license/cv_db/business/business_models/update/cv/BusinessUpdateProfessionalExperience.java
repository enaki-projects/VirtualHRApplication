package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProfessionalExperience;
import org.enaki.license.cv_db.persistence.entities.cv.ProfessionalExperience;

import java.sql.Date;

public class BusinessUpdateProfessionalExperience {
    public Long organisationId;
    public Long uid;
    public String jobName;
    public String description;
    public Date fromDate;
    public Date toDate;

    public BusinessUpdateProfessionalExperience(Long organisationId, Long uid, String jobName, String description, Date fromDate, Date toDate) {
        this.organisationId = organisationId;
        this.uid = uid;
        this.jobName = jobName;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public static ProfessionalExperience toProfessionalExperience(Long professionalExperienceId, BusinessUpdateProfessionalExperience professionalExperience) {
        return new ProfessionalExperience(
                professionalExperienceId,
                professionalExperience.organisationId,
                professionalExperience.uid,
                professionalExperience.jobName,
                professionalExperience.description,
                professionalExperience.fromDate,
                professionalExperience.toDate
        );
    }

    public static BusinessCreateProfessionalExperience toBusinessCreateProfessionalExperience(BusinessUpdateProfessionalExperience professionalExperience) {
        return new BusinessCreateProfessionalExperience(
                professionalExperience.organisationId,
                professionalExperience.uid,
                professionalExperience.jobName,
                professionalExperience.description,
                professionalExperience.fromDate,
                professionalExperience.toDate
        );
    }

    public boolean isValid() {
        return (organisationId != null &&
                uid != null &&
                jobName != null &&
                fromDate != null);
    }

    public String getRequiredFieldsMessage(){
        return "Professional Experience require fields (organisationId, jobName, fromDate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Professional Experience with value fields (organisationId, uid, jobName, fromDate): (%s, %s, %s, %s) is existent",
                organisationId, uid, jobName, fromDate, toDate);
    }
}
