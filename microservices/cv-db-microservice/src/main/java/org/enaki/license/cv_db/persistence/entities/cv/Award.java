package org.enaki.license.cv_db.persistence.entities.cv;

import java.sql.Date;

public class Award {
    public Long awardId;
    public Long uid;
    public String title;
    public String awardingInstitution;
    public Date dateOfAward;
    public String description;

    public Award(Long awardId, Long uid, String title, String awardingInstitution, Date dateOfAward, String description) {
        this.awardId = awardId;
        this.uid = uid;
        this.title = title;
        this.awardingInstitution = awardingInstitution;
        this.dateOfAward = dateOfAward;
        this.description = description;
    }
}
