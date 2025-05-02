package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Location;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LocationRowMapper implements RowMapper<Location> {
    @Override
    public Location mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Location(
                resultSet.getLong("location_id"),
                resultSet.getLong("city_id"),
                resultSet.getLong("uid"),
                resultSet.getString("type"),
                resultSet.getString("address")
        );
    }
}
