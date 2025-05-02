package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateLocation;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessLocation;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateLocation;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface ILocationService {
    Response<List<BusinessLocation>> getAllLocations();
    Response<List<BusinessLocation>> getLocationsByUserId(Long uid);
    Response<List<BusinessLocation>> getLocationsByName(String name);

    Response<BusinessLocation> getLocationById(Long id);
    Response<?> deleteLocationId(Long id);
    Response<BusinessId> createLocation(BusinessCreateLocation location);
    Response<?> updateLocation(Long id, BusinessUpdateLocation location);
}
