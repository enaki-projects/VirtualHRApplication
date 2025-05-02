package org.enaki.license.cv_db.persistence.interfaces.geography;

import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;
import org.enaki.license.cv_db.persistence.interfaces.generic.IReadRepository;

import java.util.List;

public interface IEducationalInstitutionRepository extends IReadRepository<EducationalInstitution> {
    List<EducationalInstitution> getIEducationalInstitutionsByName(String name);
    List<EducationalInstitution> getEducationalInstitutionsByCountryId(Long id);
}
