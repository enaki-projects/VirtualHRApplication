package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.EducationalExperience;
import org.enaki.license.cv_db.persistence.interfaces.cv.IEducationalExperienceRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.EducationalExperienceRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EducationalExperienceRepository extends GenericRepository<EducationalExperience> implements IEducationalExperienceRepository {
    private final IdRowMapper _idRowMapper;

    public EducationalExperienceRepository() {
        _tableName = "educational_experiences";
        _rowMapper = new EducationalExperienceRowMapper();
        _idRowMapper = new IdRowMapper("educational_experience_id");
    }

    @Override
    public Long add(EducationalExperience item) {
        String script = String.format("INSERT INTO %s (educational_institution_id, uid, qualification_title, from_date, to_date, specialty) VALUES (?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.educationalInstitutionId, item.uid, item.qualificationTitle, item.fromDate, item.toDate, item.specialty);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND qualification_title=? AND from_date=? AND specialty=? AND educational_institution_id=?", _tableName);
        List<EducationalExperience> educationalExperienceList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.qualificationTitle, item.fromDate, item.specialty, item.educationalInstitutionId);
        if (educationalExperienceList.isEmpty())
            return null;
        else
            return educationalExperienceList.get(0).educationalExperienceId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE educational_experience_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, EducationalExperience item) {
        String script = String.format("UPDATE %s SET educational_institution_id=?, uid=?, qualification_title=?, from_date=?, to_date=?, specialty=? WHERE educational_experience_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.educationalInstitutionId, item.uid, item.qualificationTitle, item.fromDate, item.toDate, item.specialty, id);
        return (numberRows > 0);
    }

    @Override
    public List<EducationalExperience> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public EducationalExperience get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE educational_experience_id = ?", _tableName);
        List<EducationalExperience> educationalExperienceList = _jdbcTemplate.query(script, _rowMapper, id);
        return educationalExperienceList.isEmpty() ? null : educationalExperienceList.get(0);
    }

    @Override
    public List<EducationalExperience> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<EducationalExperience> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(qualification_title), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public Boolean addWithId(EducationalExperience item) {
        String script = String.format("INSERT INTO %s (educational_experience_id, educational_institution_id, uid, qualification_title, from_date, to_date, specialty) VALUES (?, ?, ?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.educationalExperienceId, item.educationalInstitutionId, item.uid, item.qualificationTitle, item.fromDate, item.toDate, item.specialty);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getEducationalExperiencesIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}