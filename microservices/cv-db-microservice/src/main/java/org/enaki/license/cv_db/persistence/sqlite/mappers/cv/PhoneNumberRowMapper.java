package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PhoneNumberRowMapper implements RowMapper<PhoneNumber> {
    @Override
    public PhoneNumber mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new PhoneNumber(
                resultSet.getLong("phone_number_id"),
                resultSet.getLong("uid"),
                resultSet.getLong("country_id"),
                resultSet.getString("number")
        );
    }
}
