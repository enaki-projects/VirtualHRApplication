package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Organisation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganisationRowMapper implements RowMapper<Organisation> {

    @Override
    public Organisation mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Organisation(
                resultSet.getLong("organisation_id"),
                resultSet.getLong("city_id"),
                resultSet.getString("organisation_name"),
                resultSet.getString("address")
        );
    }
}
