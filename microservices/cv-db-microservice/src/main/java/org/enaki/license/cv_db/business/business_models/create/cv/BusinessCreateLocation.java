package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Location;

public class BusinessCreateLocation {
    public Long cityId;
    public Long uid;
    public String type;
    public String address;

    public BusinessCreateLocation(Long cityId, Long uid, String type, String address) {
        this.cityId = cityId;
        this.uid = uid;
        this.type = type;
        this.address = address;
    }

    public static Location toLocation(BusinessCreateLocation location){
        return new Location(
                Integer.toUnsignedLong(0),
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
        return "Location require fields (cityId, type)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Location with value fields (uid, cityId, type): (%s, %s, %s) is existent", uid, cityId, type);
    }
}
