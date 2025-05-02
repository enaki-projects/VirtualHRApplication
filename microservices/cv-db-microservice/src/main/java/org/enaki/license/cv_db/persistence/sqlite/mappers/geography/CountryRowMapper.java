package org.enaki.license.cv_db.persistence.sqlite.mappers.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Country(
                resultSet.getLong("country_id"),
                resultSet.getString("country_name"),
                resultSet.getString("iso3"),
                resultSet.getString("iso2"),
                resultSet.getString("phone_code"),
                resultSet.getString("capital"),
                resultSet.getString("currency")
        );
    }
}
