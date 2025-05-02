package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface ICitizenshipService {
    Response<List<String>> getUserNationalitiesByUserId(Long uid);

    Response<?> deleteUserNationalityId(Long uid, Long nationalityId);
    Response<?> addUserNationality(Long uid, Long nationalityId);
}
