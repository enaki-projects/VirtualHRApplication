package org.enaki.license.cv_db.persistence.entities.geography;

public class City {
    public Long cityId;
    public Long regionId;
    public String cityName;
    public Float latitude;
    public Float longitude;

    public City(Long cityId, Long regionId, String cityName, Float latitude, Float longitude) {
        this.cityId = cityId;
        this.regionId = regionId;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
