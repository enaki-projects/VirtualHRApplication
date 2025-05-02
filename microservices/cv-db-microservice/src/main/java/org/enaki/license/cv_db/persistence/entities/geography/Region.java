package org.enaki.license.cv_db.persistence.entities.geography;

public class Region {
    public Long regionId;
    public Long countryId;
    public String regionName;
    public String regionCode;

    public Region(Long regionId, Long countryId, String regionName, String regionCode) {
        this.regionId = regionId;
        this.countryId = countryId;
        this.regionName = regionName;
        this.regionCode = regionCode;
    }
}
