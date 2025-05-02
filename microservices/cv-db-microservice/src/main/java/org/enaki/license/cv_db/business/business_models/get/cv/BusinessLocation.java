package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Location;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessLocation {
    public Long locationId;
    public Long cityId;
    public Long uid;
    public String type;
    public String address;
    public HashMap<String, String> references = new HashMap<>();


    public BusinessLocation(Long locationId, Long cityId, Long uid, String type, String address, String serverAddress) {
        this.locationId = locationId;
        this.cityId = cityId;
        this.uid = uid;
        this.type = type;
        this.address = address;
        this.references.put("cityReference", String.format("%s/api/geography/cities/%s", serverAddress, this.cityId));
    }

    public static BusinessLocation toBusinessLocation(Location location, String serverAddress){
        return new BusinessLocation(
                location.locationId,
                location.cityId,
                location.uid,
                location.type,
                location.address,
                serverAddress
        );
    }

    public static List<BusinessLocation> toBusinessLocationList(List<Location> locationList, String serverAddress){
        return locationList.stream()
                .map(it -> BusinessLocation.toBusinessLocation(it, serverAddress))
                .collect(Collectors.toList());
    }
}
