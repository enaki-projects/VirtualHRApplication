package org.enaki.license.cv_db.business.interfaces;

import org.enaki.license.cv_db.business.business_models.create.BusinessCreateCV;
import org.enaki.license.cv_db.business.business_models.get.BusinessCV;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdateCV;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface ICVService {
    Response<List<BusinessCV>> getAllCVs();
    Response<?> getCVByUserId(Long uid);
    Response<?> createCV(BusinessCreateCV cv);
    Response<?> getCVByEmail(String email);
    Response<?> updateCV(Long uid, BusinessUpdateCV cv);
    Response<?> deleteCV(Long uid);
}
