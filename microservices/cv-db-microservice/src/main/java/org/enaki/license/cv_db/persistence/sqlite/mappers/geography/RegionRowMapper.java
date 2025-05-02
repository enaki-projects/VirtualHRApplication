package org.enaki.license.cv_db.persistence.sqlite.mappers.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Region;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegionRowMapper implements RowMapper<Region> {
    @Override
    public Region mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Region(
                resultSet.getLong("region_id"),
                resultSet.getLong("country_id"),
                resultSet.getString("region_name"),
                resultSet.getString("region_code")
        );
    }
}
