package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLanguageRowMapper implements RowMapper<UserLanguage> {
    @Override
    public UserLanguage mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new UserLanguage(
                resultSet.getLong("user_language_id"),
                resultSet.getLong("uid"),
                resultSet.getLong("language_id"),
                resultSet.getString("listening"),
                resultSet.getString("reading"),
                resultSet.getString("spoken_interaction"),
                resultSet.getString("spoken_production"),
                resultSet.getString("writing")
        );
    }
}
