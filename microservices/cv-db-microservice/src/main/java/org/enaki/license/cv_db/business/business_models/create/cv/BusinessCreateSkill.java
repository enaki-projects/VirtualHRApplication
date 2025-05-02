package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Skill;

public class BusinessCreateSkill {
    public String name;
    public String description;

    public BusinessCreateSkill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Skill toSkill(BusinessCreateSkill skill) {
        return new Skill(
                Integer.toUnsignedLong(0),
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
