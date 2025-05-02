package org.enaki.license.cv_db.business.services.user;

import org.enaki.license.cv_db.business.business_models.get.user.BusinessRole;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.enaki.license.cv_db.business.interfaces.users.IRoleService;
import org.enaki.license.cv_db.persistence.entities.users.Role;
import org.enaki.license.cv_db.persistence.interfaces.users.IRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements IRoleService {
    private final IRoleRepository _roleRepository;

    public RoleService(IRoleRepository roleRepository) {
        _roleRepository = roleRepository;
    }

    @Override
    public Response<List<BusinessRole>> getAllRoles() {
        try{
            List<Role> roleList = _roleRepository.getAll();
            return new Response<>(true, 200, BusinessRole.toBusinessRoleList(roleList));
        } catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }

    @Override
    public Response<BusinessRole> getRoleById(Long id) {
        try{
            Role role = _roleRepository.get(id);
            if (role == null){
                return new Response<>(false, 404, null, "Role Not Found");
            }
            return new Response<>(true, 200, BusinessRole.toBusinessRole(role));
        }
        catch(Exception e){
            return new Response<>(false, 400, null, e.toString());
        }
    }
}
