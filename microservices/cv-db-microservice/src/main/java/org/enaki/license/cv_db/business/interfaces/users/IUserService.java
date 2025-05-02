package org.enaki.license.cv_db.business.interfaces.users;

import org.enaki.license.cv_db.business.business_models.create.BusinessCreateUser;
import org.enaki.license.cv_db.business.business_models.get.BusinessAuthUser;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.BusinessUser;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdatePassword;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdateUser;
import org.enaki.license.cv_db.business.error_handling.Response;

import java.util.List;


public interface IUserService {
    Response<List<BusinessUser>> getAllUsers();
    Response<BusinessUser> getUserById(Long uid);
    Response<?> deleteUserById(Long uid);
    Response<BusinessUser> getUserByEmail(String email);
    Response<BusinessId> createUser(BusinessCreateUser user);
    Response<?> updateUser(Long uid, BusinessUpdateUser user);

    Response<?> login(BusinessAuthUser user);

    Response<?> revokeRole(Long uid, Long roleId);
    Response<?> grantRole(Long uid, Long roleId);

    Response<?> changePassword(Long uid, BusinessUpdatePassword user);
}
