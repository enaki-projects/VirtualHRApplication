package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Project;

import java.sql.Date;

public class BusinessCreateProject {
    public Long uid;
    public String name;
    public String description;
    public Date fromDate;
    public Date toDate;
    public String url;

    public BusinessCreateProject(Long uid, String name, String description, Date fromDate, Date toDate, String url) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.url = url;
    }

    public static Project toProject(BusinessCreateProject project){
        return new Project(
                Integer.toUnsignedLong(0),
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
        return "Project require fields (name, fromDate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Project with value fields (uid, name, fromDate): (%s, %s, %s) is existent", uid, name, fromDate);
    }
}
