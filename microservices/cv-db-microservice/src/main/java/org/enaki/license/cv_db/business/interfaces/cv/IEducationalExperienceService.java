package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateEducationalExperience;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessEducationalExperience;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateEducationalExperience;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IEducationalExperienceService {
    Response<List<BusinessEducationalExperience>> getAllEducationalExperiences();
    Response<List<BusinessEducationalExperience>> getEducationalExperiencesByUserId(Long uid);
    Response<List<BusinessEducationalExperience>> getEducationalExperiencesByName(String name);

    Response<BusinessEducationalExperience> getEducationalExperienceById(Long id);
    Response<?> deleteEducationalExperienceId(Long id);
    Response<BusinessId> createEducationalExperience(BusinessCreateEducationalExperience educationalExperience);
    Response<?> updateEducationalExperience(Long id, BusinessUpdateEducationalExperience educationalExperience);
}
