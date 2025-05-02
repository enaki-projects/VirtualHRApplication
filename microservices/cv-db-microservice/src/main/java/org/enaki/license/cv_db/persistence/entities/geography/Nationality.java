package org.enaki.license.cv_db.persistence.entities.geography;

public class Nationality {
    public Long nationalityId;
    public String nationalityName;

    public Nationality(Long nationalityId, String nationalityName) {
        this.nationalityId = nationalityId;
        this.nationalityName = nationalityName;
    }
}
