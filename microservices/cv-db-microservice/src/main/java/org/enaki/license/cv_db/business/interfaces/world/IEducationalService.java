package org.enaki.license.cv_db.business.interfaces.world;

import org.enaki.license.cv_db.business.business_models.get.geography.BusinessEducationalInstitution;
import org.enaki.license.cv_db.business.business_models.get.geography.BusinessEducationalInstitutionBasic;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IEducationalService {
    Response<List<BusinessEducationalInstitution>> getAllEducationalInstitutions();
    Response<List<BusinessEducationalInstitutionBasic>> getAllEducationalInstitutionsBasicInformation();
    Response<?> getEducationalInstitutionsByCountryId(Long id, Boolean withBasicInfo);

    Response<List<BusinessEducationalInstitution>> getEducationalInstitutionsByName(String eduInstitutionName);
    Response<List<BusinessEducationalInstitutionBasic>> getEducationalInstitutionsBasicInformationByName(String name);

    Response<BusinessEducationalInstitution> getEducationalInstitution(Long educationalInstitutionId);

}
