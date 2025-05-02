package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.interfaces.cv.ICitizenshipRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CitizenshipRepository extends GenericRepository<Long> implements ICitizenshipRepository {
    IdRowMapper _idRowMapper;

    public CitizenshipRepository() {
        _tableName = "citizenships";
        _idRowMapper = new IdRowMapper("nationality_id");
    }

    @Override
    public List<Long> getNationalitiesIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s c, nationalities n WHERE c.uid = ? AND c.nationality_id = n.nationality_id", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }

    @Override
    public Boolean delete(Long uid, Long nationalityId) {
        String script = String.format("DELETE FROM %s WHERE uid = ? AND nationality_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, uid, nationalityId);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long uid, Long nationalityId) {
        String script = String.format("INSERT INTO %s (uid, nationality_id) VALUES (?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, uid, nationalityId);
        return (numberRows > 0);
    }
}
