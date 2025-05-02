package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateHobby;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessHobby;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateHobby;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IHobbyService {
    Response<List<BusinessHobby>> getAllHobbies();
    Response<List<BusinessHobby>> getHobbiesByUserId(Long uid);
    Response<List<BusinessHobby>> getHobbiesByName(String name);

    Response<BusinessHobby> getHobbyById(Long id);
    Response<?> deleteHobbyId(Long id);
    Response<BusinessId> createHobby(BusinessCreateHobby hobby);
    Response<?> updateHobby(Long id, BusinessUpdateHobby hobby);
}
