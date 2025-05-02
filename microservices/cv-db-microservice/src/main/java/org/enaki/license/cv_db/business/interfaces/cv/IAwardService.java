package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateAward;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessAward;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateAward;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IAwardService {
    Response<List<BusinessAward>> getAllAwards();
    Response<List<BusinessAward>> getAwardsByUserId(Long uid);
    Response<List<BusinessAward>> getAwardsByName(String name);

    Response<BusinessAward> getAwardById(Long id);
    Response<?> deleteAwardId(Long id);
    Response<BusinessId> createAward(BusinessCreateAward award);
    Response<?> updateAward(Long id, BusinessUpdateAward award);
}
