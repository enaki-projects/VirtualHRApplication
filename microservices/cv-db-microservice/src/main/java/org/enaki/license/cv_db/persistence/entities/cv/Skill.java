package org.enaki.license.cv_db.persistence.entities.cv;

public class Skill {
    public Long skillId;
    public String name;
    public String description;

    public Skill(Long skillId, String name, String description) {
        this.skillId = skillId;
        this.name = name;
        this.description = description;
    }
}
