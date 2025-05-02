package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateLocation;
import org.enaki.license.cv_db.persistence.entities.cv.Location;

public class BusinessUpdateLocation {
    public Long cityId;
    public Long uid;
    public String type;
    public String address;

    public BusinessUpdateLocation(Long cityId, Long uid, String type, String address) {
        this.cityId = cityId;
        this.uid = uid;
        this.type = type;
        this.address = address;
    }

    public static Location toLocation(Long locationId, BusinessUpdateLocation location) {
        return new Location(
                locationId,
                location.cityId,
                location.uid,
                location.type,
                location.address
        );
    }

    public static BusinessCreateLocation toBusinessCreateLocation(BusinessUpdateLocation location) {
        return new BusinessCreateLocation(
                location.cityId,
                location.uid,
                location.type,
                location.address
        );
    }

    public boolean isValid() {
        return (uid != null && cityId != null && type != null);
    }

    public String getRequiredFieldsMessage(){
        return "Location require fields (uid, cityId, type)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Location with value fields (uid, cityId, type): (%s, %s, %s) is existent", uid, cityId, type);
    }
}
