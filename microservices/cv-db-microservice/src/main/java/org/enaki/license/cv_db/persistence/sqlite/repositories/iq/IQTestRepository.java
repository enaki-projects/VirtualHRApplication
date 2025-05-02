package org.enaki.license.cv_db.persistence.sqlite.repositories.iq;

import org.enaki.license.cv_db.persistence.entities.iq.IQTest;
import org.enaki.license.cv_db.persistence.interfaces.iq.IIQTestRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.iq.IQTestRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IQTestRepository extends GenericRepository<IQTest> implements IIQTestRepository {
    public IQTestRepository() {
        _tableName = "iq_tests";
        _rowMapper = new IQTestRowMapper();
    }

    @Override
    public List<IQTest> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public List<IQTest> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid=?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public IQTest getLatestByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid=? ORDER BY submitted_on DESC", _tableName);
        List<IQTest> iqTests = _jdbcTemplate.query(script, _rowMapper, uid);
        return iqTests.isEmpty() ? null : iqTests.get(0);
    }

    @Override
    public IQTest get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE iq_test_id = ?", _tableName);
        List<IQTest> iqTests = _jdbcTemplate.query(script, _rowMapper, id);
        return iqTests.isEmpty() ? null : iqTests.get(0);
    }

    @Override
    public Long add(IQTest item) {
        String script = String.format("INSERT INTO %s (uid, test_name, exact_iq, iq, iq_adjusted_by_age, time_taken_in_seconds, diagnostic, user_answers, submitted_on, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.testName, item.exactIQ, item.iq, item.iqAdjustedByAge, item.timeTakenInSeconds, item.diagnostic, item.userAnswer, item.submittedOn, item.description);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND test_name=? AND user_answers=?", _tableName);
        List<IQTest> iqTests = _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.testName, item.userAnswer);
        return iqTests.isEmpty() ? null : iqTests.get(0).iqTestId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE iq_test_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, IQTest item) {
        return false;
    }
}
