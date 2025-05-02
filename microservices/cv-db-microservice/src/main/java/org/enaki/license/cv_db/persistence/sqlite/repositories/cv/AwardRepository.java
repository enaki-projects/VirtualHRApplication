package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Award;
import org.enaki.license.cv_db.persistence.interfaces.cv.IAwardRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.AwardRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AwardRepository extends GenericRepository<Award> implements IAwardRepository {
    IdRowMapper _idRowMapper;

    public AwardRepository() {
        _tableName = "awards";
        _rowMapper = new AwardRowMapper();
        _idRowMapper = new IdRowMapper("award_id");
    }

    @Override
    public Long add(Award item) {
        String script = String.format("INSERT INTO %s (uid, title, awarding_institution, date_of_award, description) VALUES (?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.title, item.awardingInstitution, item.dateOfAward, item.description);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND title=? AND awarding_institution=? AND date_of_award=?", _tableName);
        List<Award> awardList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.title, item.awardingInstitution, item.dateOfAward);
        if (awardList.isEmpty())
            return null;
        else
            return awardList.get(0).awardId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE award_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Award item) {
        String script = String.format("UPDATE %s SET uid=?, title=?, awarding_institution=?, date_of_award=?, description=? WHERE award_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.title, item.awardingInstitution, item.dateOfAward, item.description, id);
        return (numberRows > 0);
    }

    @Override
    public List<Award> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Award get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE award_id = ?", _tableName);
        List<Award> awardList = _jdbcTemplate.query(script, _rowMapper, id);
        return awardList.isEmpty() ? null : awardList.get(0);
    }

    @Override
    public List<Award> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }


    @Override
    public List<Award> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(title), ?) > 0 OR instr(lower(awarding_institution), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), name.toLowerCase());
    }

    @Override
    public Boolean addWithId(Award item) {
        String script = String.format("INSERT INTO %s (award_id, uid, title, awarding_institution, date_of_award, description) VALUES (?, ?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.awardId, item.uid, item.title, item.awardingInstitution, item.dateOfAward, item.description);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getAwardsIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
