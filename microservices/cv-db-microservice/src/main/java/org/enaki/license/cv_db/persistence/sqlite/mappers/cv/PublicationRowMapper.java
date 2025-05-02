package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Publication;
import org.enaki.license.cv_db.persistence.sqlite.utils.DateParser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublicationRowMapper implements RowMapper<Publication> {
    @Override
    public Publication mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Date date;
        try{
            date = resultSet.getDate("year");
        } catch (Exception e){
            date = DateParser.parse(resultSet.getString("year"));
        }

        return new Publication(
                resultSet.getLong("publication_id"),
                resultSet.getLong("uid"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                date,
                resultSet.getString("url"),
                resultSet.getString("reference")
        );
    }
}
