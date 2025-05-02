package org.enaki.license.cv_db.persistence.entities.cv;

import java.sql.Date;

public class Publication {
    public Long publicationId;
    public Long uid;
    public String title;
    public String description;
    public Date year;
    public String url;
    public String reference;

    public Publication(Long publicationId, Long uid, String title, String description, Date year, String url, String reference) {
        this.publicationId = publicationId;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.year = year;
        this.url = url;
        this.reference = reference;
    }
}
