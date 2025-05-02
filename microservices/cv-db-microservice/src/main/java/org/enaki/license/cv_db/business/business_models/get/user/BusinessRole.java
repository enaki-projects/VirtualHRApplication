package org.enaki.license.cv_db.business.business_models.get.user;

import org.enaki.license.cv_db.persistence.entities.users.Role;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessRole {
    public Long roleId;
    public String roleName;
    public String description;

    public BusinessRole(Long roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }

    public static BusinessRole toBusinessRole(Role role){
        return new BusinessRole(role.roleId, role.roleName, role.description);
    }

    public static List<BusinessRole> toBusinessRoleList(List<Role> roleList){
        return roleList.stream()
                .map(BusinessRole::toBusinessRole)
                .collect(Collectors.toList());
    }
}
