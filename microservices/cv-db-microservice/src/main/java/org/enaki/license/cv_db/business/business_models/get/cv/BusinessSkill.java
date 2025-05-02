package org.enaki.license.cv_db.business.business_models.get.cv;


import org.enaki.license.cv_db.persistence.entities.cv.Skill;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessSkill {
    public Long skillId;
    public String name;
    public String description;

    public BusinessSkill(Long skillId, String name, String description) {
        this.skillId = skillId;
        this.name = name;
        this.description = description;
    }

    public static BusinessSkill toBusinessSkill(Skill skill){
        return new BusinessSkill(
                skill.skillId,
                skill.name,
                skill.description
        );
    }

    public static List<BusinessSkill> toBusinessSkillList(List<Skill> skillList){
        return skillList.stream()
                .map(BusinessSkill::toBusinessSkill)
                .collect(Collectors.toList());
    }
}
