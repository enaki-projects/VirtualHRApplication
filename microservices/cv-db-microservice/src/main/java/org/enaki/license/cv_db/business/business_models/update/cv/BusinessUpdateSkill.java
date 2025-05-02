package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateSkill;
import org.enaki.license.cv_db.persistence.entities.cv.Skill;

public class BusinessUpdateSkill {
    public String name;
    public String description;

    public BusinessUpdateSkill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Skill toSkill(Long skillId, BusinessUpdateSkill skill) {
        return new Skill(
                skillId,
                skill.name,
                skill.description
        );
    }

    public static BusinessCreateSkill toBusinessCreateSkill(BusinessUpdateSkill skill) {
        return new BusinessCreateSkill(
                skill.name,
                skill.description
        );
    }

    public boolean isValid() {
        return (name != null);
    }

    public String getRequiredFieldsMessage(){
        return "Skill require fields (name)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Skill with value fields (name): (%s) is existent", name);
    }
}
