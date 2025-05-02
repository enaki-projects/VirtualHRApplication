package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IPhoneNumberRepository extends IRepository<PhoneNumber> {
    List<PhoneNumber> getAllByUid(Long uid);
    List<PhoneNumber> getAllByName(String name);
    Boolean addWithId(PhoneNumber item);
    List<Long> getPhoneNumbersIdsByUid(Long uid);
}
