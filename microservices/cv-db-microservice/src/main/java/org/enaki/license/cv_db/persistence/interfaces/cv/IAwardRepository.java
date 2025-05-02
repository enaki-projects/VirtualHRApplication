package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Award;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IAwardRepository extends IRepository<Award> {
    List<Award> getAllByUid(Long uid);
    List<Award> getAllByName(String name);
    Boolean addWithId(Award item);
    List<Long> getAwardsIdsByUid(Long uid);
}
