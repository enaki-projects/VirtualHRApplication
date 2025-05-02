package org.enaki.license.cv_db.business.interfaces.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateProject;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.cv.BusinessProject;
import org.enaki.license.cv_db.business.business_models.update.cv.BusinessUpdateProject;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IProjectService {
    Response<List<BusinessProject>> getAllProjects();
    Response<List<BusinessProject>> getProjectsByUserId(Long uid);
    Response<List<BusinessProject>> getProjectsByName(String name);

    Response<BusinessProject> getProjectById(Long id);
    Response<?> deleteProjectId(Long id);
    Response<BusinessId> createProject(BusinessCreateProject project);
    Response<?> updateProject(Long id, BusinessUpdateProject project);
}
