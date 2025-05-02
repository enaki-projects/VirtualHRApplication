package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateUserLanguage;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessUserLanguage;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateUserLanguage;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IUserLanguageService {
    Response<List<BusinessUserLanguage>> getAllUserLanguages();
    Response<List<BusinessUserLanguage>> getUserLanguagesByUserId(Long uid);
    Response<List<BusinessUserLanguage>> getUserLanguagesByName(String name);

    Response<BusinessUserLanguage> getUserLanguageById(Long id);
    Response<?> deleteUserLanguageId(Long id);
    Response<BusinessId> createUserLanguage(BusinessCreateUserLanguage userLanguage);
    Response<?> updateUserLanguage(Long id, BusinessUpdateUserLanguage userLanguage);
}
