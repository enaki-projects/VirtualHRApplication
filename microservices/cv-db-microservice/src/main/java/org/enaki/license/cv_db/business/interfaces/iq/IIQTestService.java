package org.enaki.license.cv_db.business.interfaces.iq;

import org.enaki.license.cv_db.business.business_models.create.iq.BusinessCreateIQTest;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.error_handling.Response;

public interface IIQTestService {
    Response<?> getAllIQTests(Boolean basicInfo);
    Response<?> getIQTestById(Long iqTestId, Boolean basicInfo);
    Response<?> getIQTestsByUid(Long uid, Boolean basicInfo);
    Response<?> getLatestIQTestByUid(Long uid, Boolean basicInfo);

    Response<?> deleteIQTestById(Long iqTestId);
    Response<BusinessId> addIQTest(BusinessCreateIQTest iqTest);
    Response<?> addPermissionToRetakeTheTest(Long uid);
    Response<?> requestPermissionToRetakeTheTest(Long uid);

    Response<?> restrictPermissionToTakeTheTest(Long uid);
}
