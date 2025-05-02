package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Skill;
import org.enaki.license.cv_db.persistence.interfaces.cv.ISkillRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.SkillRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SkillRepository extends GenericRepository<Skill> implements ISkillRepository {
    private final IdRowMapper _idRowMapper;

    public SkillRepository() {
        _rowMapper = new SkillRowMapper();
        _idRowMapper = new IdRowMapper("skill_id");
    }

    @Override
    public void setTableName(String name) {
        _tableName = name;
    }

    @Override
    public String getTableName() {return _tableName;}

    @Override
    public Long add(Skill item) {
        String script = String.format("INSERT INTO %s (name, description) VALUES (?, ?)", _tableName);
        _jdbcTemplate.update(script, item.name, item.description);

        String queryScript = String.format("SELECT * from %s WHERE name=?", _tableName);
        List<Skill> skillList =  _jdbcTemplate.query(queryScript, _rowMapper, item.name);
        if (skillList.isEmpty())
            return null;
        else
            return skillList.get(0).skillId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE skill_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Skill item) {
        String script = String.format("UPDATE %s SET name=?, description=? WHERE skill_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.name, item.description, id);
        return (numberRows > 0);
    }

    @Override
    public List<Skill> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Skill get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE skill_id = ?", _tableName);
        List<Skill> skillList = _jdbcTemplate.query(script, _rowMapper, id);
        return skillList.isEmpty() ? null : skillList.get(0);
    }

    @Override
    public List<Skill> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s s, user_%s u WHERE u.uid = ? AND s.skill_id=u.skill_id", _tableName, _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<Skill> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(name), ?) > 0 ", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public Skill getSkillByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE lower(name) = ? ", _tableName);
        List<Skill> skillList =  _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
        if (skillList.isEmpty())
            return null;
        else
            return skillList.get(0);
    }

    @Override
    public Boolean addWithId(Skill item) {
        String script = String.format("INSERT INTO %s (skill_id, name, description) VALUES (?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.skillId, item.name, item.description);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getSkillsIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s s, user_%s u WHERE u.uid = ? AND s.skill_id=u.skill_id", _tableName, _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }

    @Override
    public Boolean addUserSkill(Long uid, Long skillId) {
        String script = String.format("INSERT INTO user_%s (uid, skill_id) VALUES (?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid, skillId);
        return (numberRows > 0);
    }

    @Override
    public Boolean deleteUserSkill(Long uid, Long skillId) {
        String script = String.format("DELETE FROM user_%s WHERE uid=? AND skill_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid, skillId);
        return (numberRows > 0);
    }
}
