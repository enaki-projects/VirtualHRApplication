package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.City;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessCity {
    public Long cityId;
    public Long regionId;
    public String cityName;
    public Float latitude;
    public Float longitude;
    public HashMap<String, String> references = new HashMap<>();

    public BusinessCity(Long cityId, Long regionId, String cityName, Float latitude, Float longitude, String serverAddress) {
        this.cityId = cityId;
        this.regionId = regionId;
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.references.put("regionReference", String.format("%s/api/geography/regions/%s", serverAddress, this.regionId));
    }

    public static BusinessCity toBusinessCity(City city, String serverAddress) {
        return new BusinessCity(city.cityId, city.regionId, city.cityName, city.latitude, city.longitude, serverAddress);
    }

    public static List<BusinessCity> toBusinessCityList(List<City> cityList, String serverAddress) {
        return cityList.stream()
                .map(it -> BusinessCity.toBusinessCity(it, serverAddress))
                .collect(Collectors.toList());
    }
}
