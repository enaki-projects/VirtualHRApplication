package org.enaki.license.cv_db.persistence.sqlite.mappers.users;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRoleRowMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return resultSet.getLong("role_id");
    }
}
