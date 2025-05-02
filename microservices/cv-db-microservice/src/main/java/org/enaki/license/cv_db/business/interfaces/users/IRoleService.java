package org.enaki.license.cv_db.business.interfaces.users;

import org.enaki.license.cv_db.business.business_models.get.user.BusinessRole;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;

public interface IRoleService {
    Response<List<BusinessRole>> getAllRoles();
    Response<BusinessRole> getRoleById(Long id);
}
