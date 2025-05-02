package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Region;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessRegion {
    public Long regionId;
    public Long countryId;
    public String regionName;
    public String regionCode;
    public HashMap<String, String> references = new HashMap<>();

    public BusinessRegion(Long regionId, Long countryId, String regionName, String regionCode, String serverAddress) {
        this.regionId = regionId;
        this.countryId = countryId;
        this.regionName = regionName;
        this.regionCode = regionCode;
        this.references.put("countryReference", String.format("%s/api/geography/countries/%s", serverAddress, this.countryId));
    }

    public static BusinessRegion toBusinessRegion(Region region, String serverAddress) {
        return new BusinessRegion(region.regionId, region.countryId, region.regionName, region.regionCode, serverAddress);
    }

    public static List<BusinessRegion> toBusinessRegionList(List<Region> regionList, String serverAddress) {
        return regionList.stream()
                .map(it -> BusinessRegion.toBusinessRegion(it, serverAddress))
                .collect(Collectors.toList());
    }
}
