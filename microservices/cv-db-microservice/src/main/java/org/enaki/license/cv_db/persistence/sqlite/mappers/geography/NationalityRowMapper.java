package org.enaki.license.cv_db.persistence.sqlite.mappers.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Nationality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NationalityRowMapper implements RowMapper<Nationality> {
    @Override
    public Nationality mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Nationality(
                resultSet.getLong("nationality_id"),
                resultSet.getString("nationality")
        );
    }
}
