package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Hobby;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HobbyRowMapper implements RowMapper<Hobby> {
    @Override
    public Hobby mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Hobby(
                resultSet.getLong("hobby_id"),
                resultSet.getLong("uid"),
                resultSet.getString("name"),
                resultSet.getString("description")
        );
    }
}
