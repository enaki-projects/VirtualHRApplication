package org.enaki.license.cv_db.persistence.sqlite.repositories.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Nationality;
import org.enaki.license.cv_db.persistence.interfaces.geography.INationalityRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.geography.NationalityRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NationalityRepository extends GenericRepository<Nationality>  implements INationalityRepository {
    public NationalityRepository() {
        _tableName = "nationalities";
        _rowMapper = new NationalityRowMapper();
    }

    @Override
    public List<Nationality> getNationalitiesByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(nationality), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public List<Nationality> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Nationality get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE nationality_id = ?", _tableName);
        List<Nationality> languageList = _jdbcTemplate.query(script, _rowMapper, id);
        return languageList.isEmpty() ? null : languageList.get(0);
    }
}
