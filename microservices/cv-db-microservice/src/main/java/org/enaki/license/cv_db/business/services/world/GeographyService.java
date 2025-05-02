package org.enaki.license.cv_db.business.services.world;

import org.enaki.license.cv_db.business.business_models.get.geography.*;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.world.IGeographyService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.geography.*;
import org.enaki.license.cv_db.persistence.interfaces.geography.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeographyService implements IGeographyService {
    private final ICountryRepository _countryRepository;
    private final IRegionRepository _regionRepository;
    private final ICityRepository _cityRepository;
    private final ILanguageRepository _languageRepository;
    private final INationalityRepository _nationalityRepository;
    private final ServerEnvUtils _serverEnvUtils;


    public GeographyService(ICountryRepository countryRepository, IRegionRepository regionRepository, ICityRepository cityRepository, ILanguageRepository languageRepository, INationalityRepository nationalityRepository, ServerEnvUtils serverEnvUtils) {
        _countryRepository = countryRepository;
        _regionRepository = regionRepository;
        _cityRepository = cityRepository;
        _languageRepository = languageRepository;
        _nationalityRepository = nationalityRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessCity>> getAllCities() {
        try{
            List<City> cityList = _cityRepository.getAll();
            return new Response<>(true, 200, BusinessCity.toBusinessCityList(cityList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCountry>> getAllCountries() {
        try{
            List<Country> countryList = _countryRepository.getAll();
            return new Response<>(true, 200, BusinessCountry.toBusinessCountryList(countryList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCountry>> getAllCountriesWhichHaveCities() {
        try{
            List<Country> countryList = _countryRepository.getCountriesWhichHaveCities();
            return new Response<>(true, 200, BusinessCountry.toBusinessCountryList(countryList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCountry>> getAllCountriesWhichHaveRegions() {
        try{
            List<Country> countryList = _countryRepository.getCountriesWhichHaveRegions();
            return new Response<>(true, 200, BusinessCountry.toBusinessCountryList(countryList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCountry>> getAllCountriesWhichHaveUniversities() {
        try{
            List<Country> countryList = _countryRepository.getCountriesWhichHaveUniversities();
            return new Response<>(true, 200, BusinessCountry.toBusinessCountryList(countryList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessRegion>> getAllRegions() {
        try{
            List<Region> regionList = _regionRepository.getAll();
            return new Response<>(true, 200, BusinessRegion.toBusinessRegionList(regionList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessRegion>> getRegionsByCountryId(Long id) {
        try{
            Country country = _countryRepository.get(id);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", id);
            }
            List<Region> regions = _regionRepository.getRegionsByCountryId(id);
            return new Response<>(true, 200, BusinessRegion.toBusinessRegionList(regions, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessRegion>> getRegionsWhichHaveCitiesByCountryId(Long id) {
        try{
            Country country = _countryRepository.get(id);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", id);
            }
            List<Region> regions = _regionRepository.getRegionsWhichHaveCitiesByCountryId(id);
            return new Response<>(true, 200, BusinessRegion.toBusinessRegionList(regions, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessLanguage>> getAllLanguages() {
        try{
            List<Language> languageList = _languageRepository.getAll();
            return new Response<>(true, 200, BusinessLanguage.toBusinessLanguageList(languageList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessNationality>> getAllNationalities() {
        try{
            List<Nationality> nationalityList = _nationalityRepository.getAll();
            return new Response<>(true, 200, BusinessNationality.toBusinessNationalityList(nationalityList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessCity> getCity(Long cityId) {
        try{
            City city = _cityRepository.get(cityId);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", cityId);
            }
            return new Response<>(true, 200, BusinessCity.toBusinessCity(city, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCity>> getCitiesByCountryId(Long countryId) {
        try{
            Country country = _countryRepository.get(countryId);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", countryId);
            }
            List<City> cityList = _cityRepository.getCitiesByCountryId(countryId);
            return new Response<>(true, 200, BusinessCity.toBusinessCityList(cityList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCity>> getCitiesByRegionId(Long regionId) {
        try{
            Region region = _regionRepository.get(regionId);
            if (region == null){
                return CustomResponse.getItemNotFoundResponse("Region", regionId);
            }
            List<City> cityList = _cityRepository.getCitiesByRegionId(regionId);
            return new Response<>(true, 200, BusinessCity.toBusinessCityList(cityList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessCountry> getCountry(Long countryId) {
        try{
            Country country = _countryRepository.get(countryId);
            if (country == null){
                return CustomResponse.getItemNotFoundResponse("Country", countryId);
            }
            return new Response<>(true, 200, BusinessCountry.toBusinessCountry(country));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessRegion> getRegion(Long regionId) {
        try{
            Region region = _regionRepository.get(regionId);
            if (region == null){
                return CustomResponse.getItemNotFoundResponse("Region", regionId);
            }
            return new Response<>(true, 200, BusinessRegion.toBusinessRegion(region, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessLanguage> getLanguage(Long languageId) {
        try{
            Language language = _languageRepository.get(languageId);
            if (language == null){
                return CustomResponse.getItemNotFoundResponse("Language", languageId);
            }
            return new Response<>(true, 200, BusinessLanguage.toBusinessLanguage(language));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessNationality> getNationality(Long nationalityId) {
        try{
            Nationality nationality = _nationalityRepository.get(nationalityId);
            if (nationality == null){
                return CustomResponse.getItemNotFoundResponse("Nationality", nationalityId);
            }
            return new Response<>(true, 200, BusinessNationality.toBusinessNationality(nationality));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCity>> getCitiesByName(String cityName) {
        try{
            List<City> cityList = _cityRepository.getCitiesByName(cityName);
            return new Response<>(true, 200, BusinessCity.toBusinessCityList(cityList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessCountry>> getCountriesByName(String countryName) {
        try{
            List<Country> countryList = _countryRepository.getCountriesByName(countryName);
            return new Response<>(true, 200, BusinessCountry.toBusinessCountryList(countryList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessRegion>> getRegionsByName(String regionName) {
        try{
            List<Region> regionList = _regionRepository.getRegionsByName(regionName);
            return new Response<>(true, 200, BusinessRegion.toBusinessRegionList(regionList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessLanguage>> getLanguagesByName(String languageName) {
        try{
            List<Language> languageList = _languageRepository.getLanguagesByName(languageName);
            return new Response<>(true, 200, BusinessLanguage.toBusinessLanguageList(languageList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessNationality>> getNationalitiesByName(String nationalityName) {
        try{
            List<Nationality> nationalityList = _nationalityRepository.getNationalitiesByName(nationalityName);
            return new Response<>(true, 200, BusinessNationality.toBusinessNationalityList(nationalityList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
