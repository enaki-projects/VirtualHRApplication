package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePublication;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessPublication;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdatePublication;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IPublicationService {
    Response<List<BusinessPublication>> getAllPublications();
    Response<List<BusinessPublication>> getPublicationsByUserId(Long uid);
    Response<List<BusinessPublication>> getPublicationsByName(String name);

    Response<BusinessPublication> getPublicationById(Long id);
    Response<?> deletePublicationId(Long id);
    Response<BusinessId> createPublication(BusinessCreatePublication publication);
    Response<?> updatePublication(Long id, BusinessUpdatePublication publication);
}
