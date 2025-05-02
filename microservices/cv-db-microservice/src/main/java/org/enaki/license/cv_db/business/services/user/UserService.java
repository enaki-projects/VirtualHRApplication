package org.enaki.license.cv_db.business.services.user;

import org.enaki.license.cv_db.business.business_models.create.BusinessCreateUser;
import org.enaki.license.cv_db.business.business_models.get.BusinessAuthUser;
import org.enaki.license.cv_db.business.business_models.get.BusinessAuthUserResponse;
import org.enaki.license.cv_db.business.business_models.get.BusinessId;
import org.enaki.license.cv_db.business.business_models.get.BusinessUser;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdatePassword;
import org.enaki.license.cv_db.business.business_models.update.BusinessUpdateUser;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.users.IUserService;
import org.enaki.license.cv_db.business.utils.ServerEnvUtils;
import org.enaki.license.cv_db.persistence.entities.users.Role;
import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.users.IRoleRepository;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final IUserRepository _userRepository;
    private final IRoleRepository _roleRepository;
    private final ServerEnvUtils _serverEnvUtils;
    private final PasswordEncoder _passwordEncoder;


    public UserService(IUserRepository userRepository, IRoleRepository roleRepository, ServerEnvUtils serverEnvUtils, PasswordEncoder passwordEncoder) {
        _userRepository = userRepository;
        _roleRepository = roleRepository;
        _serverEnvUtils = serverEnvUtils;
        _passwordEncoder = passwordEncoder;
    }

    @Override
    public Response<List<BusinessUser>> getAllUsers() {
        try {
            List<User> userList = _userRepository.getAll();

            List<BusinessUser> businessUserList = BusinessUser.toBusinessUserList(userList);
            for (BusinessUser businessUser: businessUserList) {
                businessUser.addRolesIds(_userRepository.getUserRolesIds(businessUser.uid), _serverEnvUtils.getServerUrlPrefix());
            }
            return new Response<>(true, 200, businessUserList);
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessUser> getUserById(Long uid) {
        try {
            User user = _userRepository.get(uid);
            if (user == null) {
                return new Response<>(false, 404, null, "User Not Found");
            }
            BusinessUser businessUser = BusinessUser.toBusinessUser(user);
            businessUser.addRolesIds(_userRepository.getUserRolesIds(uid), _serverEnvUtils.getServerUrlPrefix());
            return new Response<>(true, 200, businessUser);
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> deleteUserById(Long uid) {
        try {
            Boolean success = _userRepository.delete(uid);
            return new Response<>(success, success ? 200 : 404, null, success ? "" : "User Not Found");
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessUser> getUserByEmail(String email) {
        try {
            User user = _userRepository.getByEmail(email);
            if (user == null) {
                return new Response<>(false, 404, null, "User Not Found");
            }
            return new Response<>(true, 200, BusinessUser.toBusinessUser(user));
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessId> createUser(BusinessCreateUser user) {
        try {
            if (!user.isValid())
                return new Response<>(false, 400, null, user.getRequiredFieldsMessage());

            user.password = this._passwordEncoder.encode(user.password);
            Long uid = _userRepository.add(BusinessCreateUser.toUser(user));
            if (uid == null) {
                return new Response<>(false, 400, null, "Can't create user");
            }

            _userRepository.grantRole(uid, 1L);

            return new Response<>(true, 201, new BusinessId(uid));
        } catch (org.springframework.jdbc.UncategorizedSQLException e) {
            return new Response<>(false, 400, null, user.getDuplicatedEntityMessage());
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> updateUser(Long uid, BusinessUpdateUser user) {
        try {
            if (!user.isValid())
                return new Response<>(false, 400, null, user.getRequiredFieldsMessage());

            User foundUser = _userRepository.get(uid);
            User dbUser = BusinessUpdateUser.toUser(uid, user);

            user.password = this._passwordEncoder.encode(user.password);
            if (foundUser == null) {
                Boolean success = _userRepository.addWithId(dbUser);
                return new Response<>(success, success ? 201 : 500, null, success ? "" : "Something Went Wrong With User Update");
            }
            Boolean success = _userRepository.update(uid, BusinessUpdateUser.toUser(uid, user));
            return new Response<>(success, success ? 200 : 500, null, success ? "" : "Something Went Wrong With User Update");
        } catch (org.springframework.jdbc.UncategorizedSQLException e) {
            return new Response<>(false, 400, null, user.getDuplicatedEntityMessage());
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> login(BusinessAuthUser user) {
        try {
            if (!user.isValid())
                return new Response<>(false, 400, null, user.getRequiredFieldsMessage());

            User dbUser = _userRepository.getByEmail(user.email);
            if (dbUser == null || !_passwordEncoder.matches(user.password, dbUser.password)){
                return new Response<>(false, 404, null, "Invalid Credentials");
            }

            BusinessAuthUserResponse businessAuthUserResponse = BusinessAuthUserResponse
                    .toBusinessAuthUserResponse(dbUser)
                    .addRolesIds(_userRepository.getUserRolesIds(dbUser.uid), _serverEnvUtils.getServerUrlPrefix());

            return new Response<>(true, 200, businessAuthUserResponse);
        } catch (Exception e) {
            return new Response<>(false, 500, null, e.toString());
        }
    }

    @Override
    public Response<?> revokeRole(Long uid, Long roleId) {
        try {
            Boolean success = _userRepository.revokeRole(uid, roleId);
            return new Response<>(success, success ? 200 : 500, null, success ? "" : "Something Went Wrong With User Update");
        } catch (org.springframework.jdbc.UncategorizedSQLException e) {
            return new Response<>(false, 400, null, String.format("User %s already has role %s", uid, roleId));
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> grantRole(Long uid, Long roleId) {
        try {
            User foundUser = _userRepository.get(uid);
            if (foundUser == null) {
                return new Response<>(false, 404, null, "User Not Found");
            }

            Role role = _roleRepository.get(roleId);
            if (role == null) {
                return new Response<>(false, 404, null, "Role Not Found");
            }

            Boolean success = _userRepository.grantRole(uid, roleId);
            return new Response<>(success, success ? 200 : 500, null, success ? "" : "Something Went Wrong With User Update");
        } catch (org.springframework.jdbc.UncategorizedSQLException e) {
            return new Response<>(false, 400, null, String.format("User %s already has role %s", uid, roleId));
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<?> changePassword(Long uid, BusinessUpdatePassword userPassword) {
        try {
            if (!userPassword.isValid())
                return new Response<>(false, 400, null, userPassword.getRequiredFieldsMessage());

            User foundUser = _userRepository.get(uid);

            if (foundUser == null || !_passwordEncoder.matches(userPassword.oldPassword, foundUser.password)){
                return new Response<>(false, 404, null, "Invalid Credentials");
            }

            foundUser.password = this._passwordEncoder.encode(userPassword.password);

            Boolean success = _userRepository.update(uid, foundUser);
            return new Response<>(success, success ? 200 : 500, null, success ? "" : "Something Went Wrong With User Update");
        } catch (Exception e) {
            return new Response<>(false, 400, null, e.toString());
        }
    }
}