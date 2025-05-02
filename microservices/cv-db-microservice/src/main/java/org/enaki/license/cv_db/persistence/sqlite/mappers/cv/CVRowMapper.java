package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.CV;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class CVRowMapper implements RowMapper<CV> {
    SimpleDateFormat formatter;

    public CVRowMapper() {
        formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    }

    @Override
    public CV mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        try {
            Date birthdate = new Date(formatter.parse(resultSet.getString("birthdate")).getTime());
            return new CV(resultSet.getLong("uid"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    birthdate,
                    resultSet.getString("gender"),
                    resultSet.getString("about_me"),
                    resultSet.getString("motto"),
                    resultSet.getInt("iq_score"),
                    resultSet.getString("image_name"),
                    resultSet.getBoolean("can_take_the_iq_test"),
                    resultSet.getBoolean("requested_retake_of_iq_test"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new SQLException("Cannot parse birthdate");
        }
    }
}
