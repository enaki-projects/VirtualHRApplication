package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Award;
import org.enaki.license.cv_db.persistence.sqlite.utils.DateParser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AwardRowMapper implements RowMapper<Award> {
    @Override
    public Award mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Date date;
        try{
            date = resultSet.getDate("date_of_award");
        } catch (Exception e){
            date = DateParser.parse(resultSet.getString("date_of_award"));
        }

        return new Award(
                resultSet.getLong("award_id"),
                resultSet.getLong("uid"),
                resultSet.getString("title"),
                resultSet.getString("awarding_institution"),
                date,
                resultSet.getString("description")
        );
    }
}
