package org.enaki.license.cv_db.persistence.sqlite.repositories.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Language;
import org.enaki.license.cv_db.persistence.interfaces.geography.ILanguageRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.geography.LanguageRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LanguageRepository extends GenericRepository<Language> implements ILanguageRepository {
    public LanguageRepository() {
        _tableName = "languages";
        _rowMapper = new LanguageRowMapper();
    }

    @Override
    public List<Language> getLanguagesByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public List<Language> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Language get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE language_id = ?", _tableName);
        List<Language> languageList = _jdbcTemplate.query(script, _rowMapper, id);
        return languageList.isEmpty() ? null : languageList.get(0);
    }
}
