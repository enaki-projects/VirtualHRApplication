package org.enaki.license.cv_db.persistence.entities.cv;

public class UserNationality {
    public Long uid;
    public String nationalityId;

    public UserNationality(Long uid, String nationalityId) {
        this.uid = uid;
        this.nationalityId = nationalityId;
    }
}
