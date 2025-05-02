package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IUserLanguageRepository extends IRepository<UserLanguage> {
    List<UserLanguage> getAllByUid(Long uid);
    List<UserLanguage> getAllByName(String name);
    Boolean addWithId(UserLanguage item);
    List<Long> getUserLanguagesIdsByUid(Long uid);
}
