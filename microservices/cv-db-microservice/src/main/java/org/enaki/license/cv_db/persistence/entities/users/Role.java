package org.enaki.license.cv_db.persistence.entities.users;

public class Role {
    public Long roleId;
    public String roleName;
    public String description;

    public Role(Long roleId, String roleName, String description) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
    }
}
