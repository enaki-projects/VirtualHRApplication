package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Hobby;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IHobbyRepository extends IRepository<Hobby> {
    List<Hobby> getAllByUid(Long uid);
    List<Hobby> getAllByName(String name);
    Boolean addWithId(Hobby item);
    List<Long> getHobbiesIdsByUid(Long uid);

}
