package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.ProfessionalExperience;
import org.enaki.license.cv_db.persistence.sqlite.utils.DateParser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfessionalExperienceRowMapper implements RowMapper<ProfessionalExperience> {
    @Override
    public ProfessionalExperience mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        Date fromDate;
        Date toDate;
        try{
            fromDate = resultSet.getDate("from_date");
            toDate = resultSet.getDate("to_date");
        } catch (Exception e){
            fromDate = DateParser.parse(resultSet.getString("from_date"));
            toDate = DateParser.parse(resultSet.getString("to_date"));
        }
        return new ProfessionalExperience(
                resultSet.getLong("professional_experience_id"),
                resultSet.getLong("organisation_id"),
                resultSet.getLong("uid"),
                resultSet.getString("job_name"),
                resultSet.getString("description"),
                fromDate,
                toDate
        );
    }
}
