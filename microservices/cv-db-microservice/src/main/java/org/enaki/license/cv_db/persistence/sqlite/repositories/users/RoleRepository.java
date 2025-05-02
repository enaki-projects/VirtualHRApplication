package org.enaki.license.cv_db.persistence.sqlite.repositories.users;

import org.enaki.license.cv_db.persistence.entities.users.Role;
import org.enaki.license.cv_db.persistence.interfaces.users.IRoleRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.users.RoleRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepository extends GenericRepository<Role> implements IRoleRepository {
    public RoleRepository() {
        _tableName = "roles";
        _rowMapper = new RoleRowMapper();
    }

    @Override
    public List<Role> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Role get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE role_id = ?", _tableName);
        List<Role> roleList = _jdbcTemplate.query(script, _rowMapper, id);
        return roleList.isEmpty() ? null : roleList.get(0);
    }
}
