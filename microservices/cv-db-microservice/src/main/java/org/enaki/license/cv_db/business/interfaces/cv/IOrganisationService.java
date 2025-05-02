package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateOrganisation;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessOrganisation;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateOrganisation;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IOrganisationService {
    Response<List<BusinessOrganisation>> getAllOrganisations();
    Response<List<BusinessOrganisation>> getOrganisationsByName(String name);

    Response<BusinessOrganisation> getOrganisationById(Long id);
    Response<?> deleteOrganisationId(Long id);
    Response<BusinessId> createOrganisation(BusinessCreateOrganisation organisation);
    Response<?> updateOrganisation(Long id, BusinessUpdateOrganisation organisation);

    Response<List<BusinessOrganisation>> getOrganisationsByCityId(Long id);

    Response<List<BusinessOrganisation>> getOrganisationsByNameAndCityId(String name, Long cityId);
}
