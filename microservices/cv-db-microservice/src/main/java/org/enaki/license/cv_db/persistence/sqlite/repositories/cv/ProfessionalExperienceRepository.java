package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.ProfessionalExperience;
import org.enaki.license.cv_db.persistence.interfaces.cv.IProfessionalExperienceRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.ProfessionalExperienceRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProfessionalExperienceRepository extends GenericRepository<ProfessionalExperience> implements IProfessionalExperienceRepository {
    private final IdRowMapper _idRowMapper;

    public ProfessionalExperienceRepository() {
        _tableName = "professional_experiences";
        _rowMapper = new ProfessionalExperienceRowMapper();
        _idRowMapper = new IdRowMapper("professional_experience_id");
    }

    @Override
    public Long add(ProfessionalExperience item) {
        String script = String.format("INSERT INTO %s (organisation_id, uid, job_name, description, from_date, to_date) VALUES (?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.organisationId, item.uid, item.jobName, item.description, item.fromDate, item.toDate);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND job_name=? AND from_date=? AND organisation_id=?", _tableName);
        List<ProfessionalExperience> professionalExperienceList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.jobName, item.fromDate, item.organisationId);
        if (professionalExperienceList.isEmpty())
            return null;
        else
            return professionalExperienceList.get(0).professionalExperienceId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE professional_experience_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, ProfessionalExperience item) {
        String script = String.format("UPDATE %s SET organisation_id=?, uid=?, job_name=?, description=?, from_date=?, to_date=? WHERE professional_experience_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.organisationId, item.uid, item.jobName, item.description, item.fromDate, item.toDate, id);
        return (numberRows > 0);
    }

    @Override
    public List<ProfessionalExperience> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public ProfessionalExperience get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE professional_experience_id = ?", _tableName);
        List<ProfessionalExperience> professionalExperienceList = _jdbcTemplate.query(script, _rowMapper, id);
        return professionalExperienceList.isEmpty() ? null : professionalExperienceList.get(0);
    }

    @Override
    public List<ProfessionalExperience> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<ProfessionalExperience> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(job_name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public Boolean addWithId(ProfessionalExperience item) {
        String script = String.format("INSERT INTO %s (professional_experience_id, organisation_id, uid, job_name, description, from_date, to_date) VALUES (?, ?, ?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.professionalExperienceId, item.organisationId, item.uid, item.jobName, item.description, item.fromDate, item.toDate);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getProfessionalExperiencesIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}