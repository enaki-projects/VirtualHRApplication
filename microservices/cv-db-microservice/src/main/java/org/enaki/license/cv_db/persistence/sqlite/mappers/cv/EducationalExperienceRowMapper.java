package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;
import org.enaki.license.cv_db.persistence.sqlite.utils.DateParser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EducationalExperienceRowMapper implements RowMapper<EducationalExperience> {
    @Override
    public EducationalExperience mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Date fromDate;
        Date toDate;
        try{
            fromDate = resultSet.getDate("from_date");
            toDate = resultSet.getDate("to_date");
        } catch (Exception e){
            fromDate = DateParser.parse(resultSet.getString("from_date"));
            toDate = DateParser.parse(resultSet.getString("to_date"));
        }
        return new EducationalExperience(
                resultSet.getLong("educational_experience_id"),
                resultSet.getLong("educational_institution_id"),
                resultSet.getLong("uid"),
                resultSet.getString("specialty"),
                resultSet.getString("qualification_title"),
                fromDate,
                toDate
        );
    }
}
