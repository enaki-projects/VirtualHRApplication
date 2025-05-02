package org.enaki.license.cv_db.persistence.interfaces.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Region;
import org.enaki.license.cv_db.persistence.interfaces.generic.IReadRepository;

import java.util.List;

public interface IRegionRepository extends IReadRepository<Region> {
    List<Region> getRegionsByName(String name);
    List<Region> getRegionsByCountryId(Long id);
    List<Region> getRegionsWhichHaveCitiesByCountryId(Long id);
}
