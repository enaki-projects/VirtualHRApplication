package org.enaki.license.cv_db.persistence.entities.cv;

public class Location {
    public Long locationId;
    public Long cityId;
    public Long uid;
    public String type;
    public String address;

    public Location(Long locationId, Long cityId, Long uid, String type, String address) {
        this.locationId = locationId;
        this.cityId = cityId;
        this.uid = uid;
        this.type = type;
        this.address = address;
    }
}
