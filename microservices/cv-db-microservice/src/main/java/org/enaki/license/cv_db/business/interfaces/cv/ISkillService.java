package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateSkill;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessSkill;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateSkill;
import org.enaki.license.cv_db.business.companion.SkillType;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface ISkillService {
    void setSkillType(SkillType skillType);
    Response<List<BusinessSkill>> getAllSkills();
    Response<List<BusinessSkill>> getSkillsByUserId(Long uid);
    Response<List<BusinessSkill>> getSkillsByName(String name);

    Response<BusinessSkill> getSkillById(Long id);
    Response<?> deleteSkillId(Long id);
    Response<BusinessId> createSkill(BusinessCreateSkill skill);
    Response<?> updateSkill(Long id, BusinessUpdateSkill skill);

    Response<?> addUserSkill(Long uid, String skillName);
    Response<?> deleteUserSkill(Long uid, String skillName);

    Response<BusinessSkill> getSkillByName(String name);
}
