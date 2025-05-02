package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Publication;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IPublicationRepository extends IRepository<Publication> {
    List<Publication> getAllByUid(Long uid);
    List<Publication> getAllByName(String name);
    Boolean addWithId(Publication item);
    List<Long> getPublicationsIdsByUid(Long uid);
}
