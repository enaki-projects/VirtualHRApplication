package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProfessionalExperience;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessProfessionalExperience;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateProfessionalExperience;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IProfessionalExperienceService {
    Response<List<BusinessProfessionalExperience>> getAllProfessionalExperiences();
    Response<List<BusinessProfessionalExperience>> getProfessionalExperiencesByUserId(Long uid);
    Response<List<BusinessProfessionalExperience>> getProfessionalExperiencesByName(String name);

    Response<BusinessProfessionalExperience> getProfessionalExperienceById(Long id);
    Response<?> deleteProfessionalExperienceId(Long id);
    Response<BusinessId> createProfessionalExperience(BusinessCreateProfessionalExperience professionalExperience);
    Response<?> updateProfessionalExperience(Long id, BusinessUpdateProfessionalExperience professionalExperience);
}
