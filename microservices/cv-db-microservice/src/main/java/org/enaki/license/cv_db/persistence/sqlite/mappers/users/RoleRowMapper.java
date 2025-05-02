package org.enaki.license.cv_db.persistence.sqlite.mappers.users;

import org.enaki.license.cv_db.persistence.entities.users.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RoleRowMapper implements RowMapper<Role> {
    @Override
    public Role mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Role(
                resultSet.getLong("role_id"),
                resultSet.getString("role_name"),
                resultSet.getString("description"));
    }
}
