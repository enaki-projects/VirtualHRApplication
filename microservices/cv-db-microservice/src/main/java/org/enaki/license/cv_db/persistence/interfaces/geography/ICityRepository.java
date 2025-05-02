package org.enaki.license.cv_db.persistence.interfaces.geography;

import org.enaki.license.cv_db.persistence.entities.geography.City;
import org.enaki.license.cv_db.persistence.interfaces.generic.IReadRepository;

import java.util.List;

public interface ICityRepository extends IReadRepository<City> {
    List<City> getCitiesByName(String name);

    List<City> getCitiesByCountryId(Long countryId);

    List<City> getCitiesByRegionId(Long regionId);
}
