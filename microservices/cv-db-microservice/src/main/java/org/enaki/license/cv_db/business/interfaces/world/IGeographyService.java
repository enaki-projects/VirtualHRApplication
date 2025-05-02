package org.enaki.license.cv_db.business.interfaces.world;

import org.enaki.license.cv_db.business.business_models.get.geography.*;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IGeographyService {
    Response<List<BusinessCity>> getAllCities();
    Response<List<BusinessCity>> getCitiesByCountryId(Long countryId);
    Response<List<BusinessCity>> getCitiesByRegionId(Long regionId);
    Response<List<BusinessCountry>> getAllCountries();
    Response<List<BusinessCountry>> getAllCountriesWhichHaveCities();
    Response<List<BusinessCountry>> getAllCountriesWhichHaveRegions();
    Response<List<BusinessCountry>> getAllCountriesWhichHaveUniversities();

    Response<List<BusinessRegion>> getAllRegions();

    Response<List<BusinessLanguage>> getAllLanguages();
    Response<List<BusinessNationality>> getAllNationalities();
    Response<List<BusinessRegion>> getRegionsByCountryId(Long id);
    Response<List<BusinessRegion>> getRegionsWhichHaveCitiesByCountryId(Long id);


    Response<BusinessCity> getCity(Long cityId);
    Response<BusinessCountry> getCountry(Long countryId);
    Response<BusinessRegion> getRegion(Long regionId);
    Response<BusinessLanguage> getLanguage(Long languageId);
    Response<BusinessNationality> getNationality(Long nationalityId);

    Response<List<BusinessCity>> getCitiesByName(String cityName);
    Response<List<BusinessCountry>> getCountriesByName(String countryName);
    Response<List<BusinessRegion>> getRegionsByName(String regionName);
    Response<List<BusinessLanguage>> getLanguagesByName(String languageName);
    Response<List<BusinessNationality>> getNationalitiesByName(String nationalityName);

}
