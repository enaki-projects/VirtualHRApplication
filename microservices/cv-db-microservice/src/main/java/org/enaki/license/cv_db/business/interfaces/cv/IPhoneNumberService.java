package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePhoneNumber;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessPhoneNumber;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdatePhoneNumber;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IPhoneNumberService {
    Response<List<BusinessPhoneNumber>> getAllPhoneNumbers();
    Response<List<BusinessPhoneNumber>> getPhoneNumbersByUserId(Long uid);
    Response<List<BusinessPhoneNumber>> getPhoneNumbersByName(String name);

    Response<BusinessPhoneNumber> getPhoneNumberById(Long id);
    Response<?> deletePhoneNumberId(Long id);
    Response<BusinessId> createPhoneNumber(BusinessCreatePhoneNumber phoneNumber);
    Response<?> updatePhoneNumber(Long id, BusinessUpdatePhoneNumber phoneNumber);
}
