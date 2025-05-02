package org.enaki.license.cv_db.persistence.sqlite.mappers.geography;

import org.enaki.license.cv_db.persistence.entities.geography.City;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CityRowMapper implements RowMapper<City> {
    @Override
    public City mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new City(
                resultSet.getLong("city_id"),
                resultSet.getLong("region_id"),
                resultSet.getString("city_name"),
                resultSet.getFloat("latitude"),
                resultSet.getFloat("longitude")
        );
    }
}
