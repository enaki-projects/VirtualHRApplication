package org.enaki.license.cv_db.persistence.interfaces.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Nationality;
import org.enaki.license.cv_db.persistence.interfaces.generic.IReadRepository;

import java.util.List;

public interface INationalityRepository extends IReadRepository<Nationality> {
    List<Nationality> getNationalitiesByName(String name);
}
