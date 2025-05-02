package org.enaki.license.cv_db.persistence.sqlite.mappers.iq;

import org.enaki.license.cv_db.persistence.entities.iq.IQTest;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IQTestRowMapper implements RowMapper<IQTest> {
    @Override
    public IQTest mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new IQTest(
                resultSet.getLong("iq_test_id"),
                resultSet.getLong("uid"),
                resultSet.getString("test_name"),
                resultSet.getFloat("exact_iq"),
                resultSet.getInt("iq"),
                resultSet.getInt("iq_adjusted_by_age"),
                resultSet.getInt("time_taken_in_seconds"),
                resultSet.getString("diagnostic"),
                resultSet.getString("user_answers"),
                resultSet.getDate("submitted_on"),
                resultSet.getString("description")
        );
    }
}
