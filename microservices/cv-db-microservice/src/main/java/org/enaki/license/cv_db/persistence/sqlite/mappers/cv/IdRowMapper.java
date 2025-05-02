package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdRowMapper implements RowMapper<Long> {
    private final String _idFieldName;
    public IdRowMapper(String idFieldName) {
        _idFieldName = idFieldName;
    }

    @Override
    public Long mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return resultSet.getLong(_idFieldName);
    }
}
