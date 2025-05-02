package org.enaki.license.cv_db.business.services.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateLocation;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessLocation;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateLocation;
import org.enaki.license.cv_db.business.error_handling.CustomResponse;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.cv.ILocationService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.cv.Location;
import org.enaki.license.cv_db.persistence.entities.geography.City;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.cv.ILocationRepository;
import org.enaki.license.cv_db.persistence.interfaces.geography.ICityRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService implements ILocationService {
    private final ILocationRepository _locationRepository;
    private final IUserRepository _userRepository;
    private final ICityRepository _cityRepository;
    private final ServerEnvUtils _serverEnvUtils;

    public LocationService(ILocationRepository locationRepository, IUserRepository userRepository, ICityRepository cityRepository, ServerEnvUtils serverEnvUtils) {
        _locationRepository = locationRepository;
        _userRepository = userRepository;
        _cityRepository = cityRepository;
        _serverEnvUtils = serverEnvUtils;
    }

    @Override
    public Response<List<BusinessLocation>> getAllLocations() {
        try{
            List<Location> locationList = _locationRepository.getAll();
            return new Response<>(true, 200, BusinessLocation.toBusinessLocationList(locationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessLocation>> getLocationsByUserId(Long uid) {
        try{
            List<Location> locationList = _locationRepository.getAllByUid(uid);
            return new Response<>(true, 200, BusinessLocation.toBusinessLocationList(locationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<List<BusinessLocation>> getLocationsByName(String name) {
        try{
            List<Location> locationList = _locationRepository.getAllByName(name);
            return new Response<>(true, 200, BusinessLocation.toBusinessLocationList(locationList, _serverEnvUtils.getServerUrlPrefix()));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessLocation> getLocationById(Long id) {
        try{
            Location location = _locationRepository.get(id);
            if (location == null){
                return CustomResponse.getItemNotFoundResponse("Location", id);
            }
            return new Response<>(true, 200, BusinessLocation.toBusinessLocation(location, _serverEnvUtils.getServerUrlPrefix()));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteLocationId(Long id) {
        try{
            Boolean success = _locationRepository.delete(id);
            return new Response<>(success, success ? 204: 404, null,  success ? "": "Location Not Found");
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessId> createLocation(BusinessCreateLocation location) {
        try{
            if (!location.isValid())
                return new Response<>(false, 400, null, location.getRequiredFieldsMessage());
            User user = _userRepository.get(location.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", location.uid);
            }
            City city = _cityRepository.get(location.cityId);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", location.cityId);
            }

            Long id = _locationRepository.add(BusinessCreateLocation.toLocation(location));
            if (id == null){
                return new Response<>(false, 400, null, "Can't create location");
            }
            return new Response<>(true, 201, new BusinessId(id));
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(location.getDuplicatedEntityMessage());
        } catch(Exception e){
            return new Response<>(false,  400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateLocation(Long id, BusinessUpdateLocation location) {
        try{
            if (!location.isValid())
                return new Response<>(false, 400, null, location.getRequiredFieldsMessage());
            User user = _userRepository.get(location.uid);
            if (user == null){
                return CustomResponse.getItemNotFoundResponse("User", location.uid);
            }
            City city = _cityRepository.get(location.cityId);
            if (city == null){
                return CustomResponse.getItemNotFoundResponse("City", location.cityId);
            }

            Location foundLocation = _locationRepository.get(id);
            Location dbLocation = BusinessUpdateLocation.toLocation(id, location);
            if (foundLocation == null){
                Boolean success = _locationRepository.addWithId(dbLocation);
                return new Response<>(success, success ? 201: 500, null,  success ? "": "Something Went Wrong With Location Update");
            }
            Boolean success = _locationRepository.update(id, BusinessUpdateLocation.toLocation(id, location));
            return new Response<>(success, success ? 204: 500, null,  success ? "": "Something Went Wrong With Location Update");
        }
        catch(org.springframework.jdbc.UncategorizedSQLException e){
            return CustomResponse.getItemIsExistentResponse(location.getDuplicatedEntityMessage());
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
