package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Hobby;
import org.enaki.license.cv_db.persistence.interfaces.cv.IHobbyRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.HobbyRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HobbyRepository extends GenericRepository<Hobby> implements IHobbyRepository {
    private final IdRowMapper _idRowMapper;

    public HobbyRepository() {
        _tableName = "hobbies";
        _rowMapper = new HobbyRowMapper();
        _idRowMapper = new IdRowMapper("hobby_id");
    }

    @Override
    public Long add(Hobby item) {
        String script = String.format("INSERT INTO %s (uid, name, description) VALUES (?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.name, item.description);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND name=?", _tableName);
        List<Hobby> hobbyList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.name);
        if (hobbyList.isEmpty())
            return null;
        else
            return hobbyList.get(0).hobbyId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE hobby_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Hobby item) {
        String script = String.format("UPDATE %s SET uid=?, name=?, description=? WHERE hobby_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.name, item.description, id);
        return (numberRows > 0);
    }

    @Override
    public List<Hobby> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Hobby get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE hobby_id = ?", _tableName);
        List<Hobby> hobbyList = _jdbcTemplate.query(script, _rowMapper, id);
        return hobbyList.isEmpty() ? null : hobbyList.get(0);
    }

    @Override
    public List<Hobby> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<Hobby> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(name), ?) > 0 ", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public Boolean addWithId(Hobby item) {
        String script = String.format("INSERT INTO %s (hobby_id, uid, name, description) VALUES (?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.hobbyId, item.uid, item.name, item.description);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getHobbiesIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
