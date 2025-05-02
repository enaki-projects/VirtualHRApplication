package org.enaki.license.cv_db.persistence.sqlite.mappers.geography;

import org.enaki.license.cv_db.persistence.entities.geography.EducationalInstitution;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EducationalInstitutionRowMapper implements RowMapper<EducationalInstitution> {
    @Override
    public EducationalInstitution mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new EducationalInstitution(
                resultSet.getLong("educational_institution_id"),
                resultSet.getLong("country_id"),
                resultSet.getString("institution_name"),
                resultSet.getString("alt_institution_name"),
                resultSet.getString("address"),
                resultSet.getString("telephone"),
                resultSet.getString("website"),
                resultSet.getString("email"),
                resultSet.getString("funding"),
                resultSet.getString("accrediting_agency")
        );
    }
}
