package org.enaki.license.cv_db.persistence.sqlite.mappers.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Language;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageRowMapper implements RowMapper<Language> {
    @Override
    public Language mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Language(
                resultSet.getLong("language_id"),
                resultSet.getString("name"),
                resultSet.getString("code"),
                resultSet.getString("native_name")
        );
    }
}
