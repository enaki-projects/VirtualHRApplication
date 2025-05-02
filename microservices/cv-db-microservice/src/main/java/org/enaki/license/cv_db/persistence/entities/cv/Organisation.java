package org.enaki.license.cv_db.persistence.entities.cv;

public class Organisation {
    public Long organisationId;
    public Long cityId;
    public String organisationName;
    public String address;

    public Organisation(Long organisationId, Long cityId, String organisationName, String address) {
        this.organisationId = organisationId;
        this.cityId = cityId;
        this.organisationName = organisationName;
        this.address = address;
    }
}
