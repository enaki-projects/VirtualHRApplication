package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.ProfessionalExperience;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessProfessionalExperience {
    public Long professionalExperienceId;
    public Long organisationId;
    public Long uid;
    public String jobName;
    public String description;
    public Date fromDate;
    public Date toDate;
    public HashMap<String, String> references = new HashMap<>();


    public BusinessProfessionalExperience(Long professionalExperienceId, Long organisationId, Long uid, String jobName, String description, Date fromDate, Date toDate, String serverAddress) {
        this.professionalExperienceId = professionalExperienceId;
        this.organisationId = organisationId;
        this.uid = uid;
        this.jobName = jobName;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.references.put("organisationReference", String.format("%s/api/organisations/%s", serverAddress, this.organisationId));
    }

    public static BusinessProfessionalExperience toBusinessProfessionalExperience(ProfessionalExperience professionalExperience, String serverAddress){
        return new BusinessProfessionalExperience(
                professionalExperience.professionalExperienceId,
                professionalExperience.organisationId,
                professionalExperience.uid,
                professionalExperience.jobName,
                professionalExperience.description,
                professionalExperience.fromDate,
                professionalExperience.toDate,
                serverAddress
        );
    }

    public static List<BusinessProfessionalExperience> toBusinessProfessionalExperienceList(List<ProfessionalExperience> professionalExperienceList, String serverAddress){
        return professionalExperienceList.stream()
                .map(it -> BusinessProfessionalExperience.toBusinessProfessionalExperience(it, serverAddress))
                .collect(Collectors.toList());
    }
}
