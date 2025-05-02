package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Project;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessProject {
    public Long projectId;
    public Long uid;
    public String name;
    public String description;
    public Date fromDate;
    public Date toDate;
    public String url;


    public BusinessProject(Long projectId, Long uid, String name, String description, Date fromDate, Date toDate, String url) {
        this.projectId = projectId;
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.url = url;
    }

    public static BusinessProject toBusinessProject(Project project){
        return new BusinessProject(
                project.projectId,
                project.uid,
                project.name,
                project.description,
                project.fromDate,
                project.toDate,
                project.url
        );
    }

    public static List<BusinessProject> toBusinessProjectList(List<Project> projectList){
        return projectList.stream()
                .map(BusinessProject::toBusinessProject)
                .collect(Collectors.toList());
    }
}
