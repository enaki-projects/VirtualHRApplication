package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.CV;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

public interface ICVRepository extends IRepository<CV> {
    Boolean delete_photo(Long uid);
    Boolean update_photo(Long uid, String imageName);
    Boolean updateIqTestStatus(Long uid, Integer iq);
    Boolean addPermissionToRetakeTheTest(Long uid);

    Boolean requestPermissionToRetakeTheTest(Long uid);

    Boolean restrictPermissionToTakeTheTest(Long uid);
}