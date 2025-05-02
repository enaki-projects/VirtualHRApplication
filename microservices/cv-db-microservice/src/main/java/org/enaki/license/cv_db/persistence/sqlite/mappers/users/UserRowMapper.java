package org.enaki.license.cv_db.persistence.sqlite.mappers.users;

import org.enaki.license.cv_db.persistence.entities.users.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new User(resultSet.getLong("uid"), resultSet.getString("email"),
                resultSet.getString("password"));
    }
}
