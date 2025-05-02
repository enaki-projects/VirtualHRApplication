package org.enaki.license.cv_db.persistence.interfaces.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Country;
import org.enaki.license.cv_db.persistence.interfaces.generic.IReadRepository;

import java.util.List;

public interface ICountryRepository extends IReadRepository<Country> {
    List<Country> getCountriesByName(String name);
    List<Country> getCountriesWhichHaveRegions();

    List<Country> getCountriesWhichHaveCities();

    List<Country> getCountriesWhichHaveUniversities();
}
