package org.enaki.license.cv_db.persistence.interfaces.cv;

import java.util.List;

public interface ICitizenshipRepository {
    List<Long> getNationalitiesIdsByUid(Long uid);
    Boolean delete(Long uid, Long nationalityId);
    Boolean update(Long uid, Long nationalityId);
}
