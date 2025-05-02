package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.ProfessionalExperience;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IProfessionalExperienceRepository extends IRepository<ProfessionalExperience> {
    List<ProfessionalExperience> getAllByUid(Long uid);
    List<ProfessionalExperience> getAllByName(String name);
    Boolean addWithId(ProfessionalExperience item);
    List<Long> getProfessionalExperiencesIdsByUid(Long uid);

}
