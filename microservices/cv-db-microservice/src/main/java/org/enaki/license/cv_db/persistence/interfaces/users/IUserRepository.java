package org.enaki.license.cv_db.persistence.interfaces.users;

import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.generic.IRepository;

import java.util.List;

public interface IUserRepository extends IRepository<User> {

    User getByEmail(String email);
    Boolean addWithId(User item);
    User getByCredentials(String email, String passwordHash);
    List<Long> getUserRolesIds(Long uid);

    Boolean grantRole(Long uid, Long roleId);

    Boolean revokeRole(Long uid, Long roleId);
}
