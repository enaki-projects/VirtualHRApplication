package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Skill;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface ISkillRepository extends IRepository<Skill> {
    void setTableName(String name);
    String getTableName();

    List<Skill> getAllByUid(Long uid);
    List<Skill> getAllByName(String name);
    Skill getSkillByName(String name);
    Boolean addWithId(Skill item);
    List<Long> getSkillsIdsByUid(Long uid);
    Boolean addUserSkill(Long uid, Long skillId);
    Boolean deleteUserSkill(Long uid, Long skillId);
}
