package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.CV;
import org.enaki.license.cv_db.persistence.interfaces.cv.ICVRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.CVRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CVRepository extends GenericRepository<CV> implements ICVRepository {
    public CVRepository() {
        _tableName = "cv";
        _rowMapper = new CVRowMapper();
    }

    @Override
    public List<CV> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public CV get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        List<CV> cvList = _jdbcTemplate.query(script, _rowMapper, id);
        return cvList.isEmpty() ? null : cvList.get(0);
    }

    @Override
    public Long add(CV item) {
        String script = String.format("INSERT INTO %s (uid, first_name, last_name, email, birthdate, gender, about_me, motto, iq_score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.firstname, item.lastname, item.email,
                item.birthdate.toString(), item.gender, item.aboutMe, item.motto, item.iqScore);
        return item.uid;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE uid = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, CV item) {
        String script = String.format("UPDATE %s SET first_name=?, last_name=?, email=?, birthdate=?, gender=?, about_me=?, motto=?, iq_score=? WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.firstname, item.lastname, item.email, item.birthdate.toString(), item.gender, item.aboutMe, item.motto, item.iqScore, id);

        return (numberRows > 0);
    }

    @Override
    public Boolean delete_photo(Long uid) {
        String script = String.format("UPDATE %s SET image_name=null WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid);
        return (numberRows > 0);
    }

    @Override
    public Boolean update_photo(Long uid, String imageName) {
        String script = String.format("UPDATE %s SET image_name=? WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, imageName, uid);
        return (numberRows > 0);
    }

    @Override
    public Boolean updateIqTestStatus(Long uid, Integer iq) {
        String script = String.format("UPDATE %s SET iq_score=?, can_take_the_iq_test=0, requested_retake_of_iq_test=0 WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, iq, uid);
        return (numberRows > 0);
    }

    @Override
    public Boolean addPermissionToRetakeTheTest(Long uid) {
        String script = String.format("UPDATE %s SET can_take_the_iq_test=1, requested_retake_of_iq_test=0 WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid);
        return (numberRows > 0);
    }

    @Override
    public Boolean requestPermissionToRetakeTheTest(Long uid) {
        String script = String.format("UPDATE %s SET can_take_the_iq_test=0, requested_retake_of_iq_test=1 WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid);
        return (numberRows > 0);
    }

    @Override
    public Boolean restrictPermissionToTakeTheTest(Long uid) {
        String script = String.format("UPDATE %s SET can_take_the_iq_test=0, requested_retake_of_iq_test=0 WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid);
        return (numberRows > 0);
    }
}
