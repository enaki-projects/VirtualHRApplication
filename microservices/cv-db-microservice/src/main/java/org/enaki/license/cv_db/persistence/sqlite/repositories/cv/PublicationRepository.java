package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Publication;
import org.enaki.license.cv_db.persistence.interfaces.cv.IPublicationRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.PublicationRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PublicationRepository extends GenericRepository<Publication> implements IPublicationRepository {
    private final IdRowMapper _idRowMapper;

    public PublicationRepository() {
        _tableName = "publications";
        _rowMapper = new PublicationRowMapper();
        _idRowMapper = new IdRowMapper("publication_id");
    }

    @Override
    public Long add(Publication item) {
        String script = String.format("INSERT INTO %s (uid, title, description, year, url, reference) VALUES (?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.title, item.description, item.year, item.url, item.reference);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND title=? AND year=?", _tableName);
        List<Publication> publicationList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.title, item.year);
        if (publicationList.isEmpty())
            return null;
        else
            return publicationList.get(0).publicationId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE publication_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Publication item) {
        String script = String.format("UPDATE %s SET uid=?, title=?, description=?, year=?, url=?, reference=? WHERE publication_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.title, item.description, item.year, item.url, item.reference, id);
        return (numberRows > 0);
    }

    @Override
    public List<Publication> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Publication get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE publication_id = ?", _tableName);
        List<Publication> publicationList = _jdbcTemplate.query(script, _rowMapper, id);
        return publicationList.isEmpty() ? null : publicationList.get(0);
    }

    @Override
    public List<Publication> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<Publication> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(title), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public Boolean addWithId(Publication item) {
        String script = String.format("INSERT INTO %s (publication_id, uid, title, description, year, url, reference) VALUES (?, ?, ?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.publicationId, item.uid, item.title, item.description, item.year, item.url, item.reference);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getPublicationsIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
