package org.enaki.license.cv_db.persistence.sqlite.mappers.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Skill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillRowMapper implements RowMapper<Skill> {
    @Override
    public Skill mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
        return new Skill(
                resultSet.getLong("skill_id"),
                resultSet.getString("name"),
                resultSet.getString("description")
        );
    }
}
