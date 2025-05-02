package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Location;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface ILocationRepository extends IRepository<Location> {
    List<Location> getAllByUid(Long uid);
    List<Location> getAllByName(String name);
    Boolean addWithId(Location item);
    List<Long> getLocationsIdsByUid(Long uid);
}
