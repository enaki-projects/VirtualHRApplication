package org.enaki.license.cv_db.persistence.entities.cv;

import java.sql.Date;

public class Project {
    public Long projectId;
    public Long uid;
    public String name;
    public String description;
    public Date fromDate;
    public Date toDate;
    public String url;

    public Project(Long projectId, Long uid, String name, String description, Date fromDate, Date toDate, String url) {
        this.projectId = projectId;
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.url = url;
    }
}
