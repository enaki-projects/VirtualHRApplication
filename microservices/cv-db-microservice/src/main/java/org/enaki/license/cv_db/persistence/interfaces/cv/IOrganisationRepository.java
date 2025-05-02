package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Organisation;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IOrganisationRepository extends IRepository<Organisation> {
    List<Organisation> getAllByName(String name);
    Boolean addWithId(Organisation item);

    List<Organisation> getByCityId(Long id);

    List<Organisation> getAllByNameAndCityId(String name, Long cityId);
}
