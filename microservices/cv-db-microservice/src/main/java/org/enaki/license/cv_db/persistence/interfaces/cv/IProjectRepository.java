package org.enaki.license.cv_db.persistence.interfaces.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Project;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IProjectRepository extends IRepository<Project> {
    List<Project> getAllByUid(Long uid);
    List<Project> getAllByName(String name);
    Boolean addWithId(Project item);
    List<Long> getProjectsIdsByUid(Long uid);
}
