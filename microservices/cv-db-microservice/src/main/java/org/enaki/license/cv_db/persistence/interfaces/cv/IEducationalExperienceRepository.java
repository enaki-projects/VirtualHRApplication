package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IEducationalExperienceRepository extends IRepository<EducationalExperience> {
    List<EducationalExperience> getAllByUid(Long uid);
    List<EducationalExperience> getAllByName(String name);
    Boolean addWithId(EducationalExperience item);
    List<Long> getEducationalExperiencesIdsByUid(Long uid);

}
