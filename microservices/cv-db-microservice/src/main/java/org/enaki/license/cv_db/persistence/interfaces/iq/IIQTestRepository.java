package org.enaki.license.cv_db.persistence.interfaces.iq;

import org.enaki.license.cv_db.persistence.entities.iq.IQTest;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IIQTestRepository extends IRepository<IQTest> {
    List<IQTest> getAllByUid(Long uid);
    IQTest getLatestByUid(Long uid);
}
