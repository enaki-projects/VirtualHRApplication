package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProject;
import org.enaki.license.cv_db.persistence.entities.cv.Project;

import java.sql.Date;

public class BusinessUpdateProject {
    public Long uid;
    public String name;
    public String description;
    public Date fromDate;
    public Date toDate;
    public String url;

    public BusinessUpdateProject(Long uid, String name, String description, Date fromDate, Date toDate, String url) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.url = url;
    }

    public static Project toProject(Long projectId, BusinessUpdateProject project) {
        return new Project(
                projectId,
                project.uid,
                project.name,
                project.description,
                project.fromDate,
                project.toDate,
                project.url
        );
    }

    public static BusinessCreateProject toBusinessCreateProject(BusinessUpdateProject project) {
        return new BusinessCreateProject(
                project.uid,
                project.name,
                project.description,
                project.fromDate,
                project.toDate,
                project.url
        );
    }

    public boolean isValid() {
        return (uid != null && name != null && fromDate != null);
    }

    public String getRequiredFieldsMessage(){
        return "Project require fields (uid, name, fromDate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Project with value fields (uid, name, fromDate): (%s, %s, %s) is existent", uid, name, fromDate);
    }
}
