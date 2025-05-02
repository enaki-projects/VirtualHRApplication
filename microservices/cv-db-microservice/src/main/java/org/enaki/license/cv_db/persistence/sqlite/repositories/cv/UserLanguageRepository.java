package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;
import org.enaki.license.cv_db.persistence.interfaces.cv.IUserLanguageRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.UserLanguageRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserLanguageRepository extends GenericRepository<UserLanguage> implements IUserLanguageRepository {
    private final IdRowMapper _idRowMapper;

    public UserLanguageRepository() {
        _tableName = "user_languages";
        _rowMapper = new UserLanguageRowMapper();
        _idRowMapper = new IdRowMapper("user_language_id");
    }

    @Override
    public Long add(UserLanguage item) {
        String script = String.format("INSERT INTO %s (uid, language_id, listening, reading, spoken_interaction, spoken_production, writing) VALUES (?, ?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.languageId, item.listening, item.reading, item.spokenInteraction, item.spokenProduction, item.writing);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND language_id=?", _tableName);
        List<UserLanguage> userLanguageList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.languageId);
        if (userLanguageList.isEmpty())
            return null;
        else
            return userLanguageList.get(0).userLanguageId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE user_language_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, UserLanguage item) {
        String script = String.format("UPDATE %s SET uid=?, language_id=?, listening=?, reading=?, spoken_interaction=?, spoken_production=?, writing=? WHERE user_language_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.languageId, item.listening, item.reading, item.spokenInteraction, item.spokenProduction, item.writing, id);
        return (numberRows > 0);
    }

    @Override
    public List<UserLanguage> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public UserLanguage get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE user_language_id = ?", _tableName);
        List<UserLanguage> userLanguageList = _jdbcTemplate.query(script, _rowMapper, id);
        return userLanguageList.isEmpty() ? null : userLanguageList.get(0);
    }

    @Override
    public List<UserLanguage> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<UserLanguage> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s, languages l WHERE language_id=l.language_id AND (instr(lower(l.name), ?) > 0 OR instr(lower(l.native_name), ?) > 0)", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), name.toLowerCase());
    }

    @Override
    public Boolean addWithId(UserLanguage item) {
        String script = String.format("INSERT INTO %s (user_language_id, uid, language_id, listening, reading, spoken_interaction, spoken_production, writing) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.userLanguageId, item.uid, item.languageId, item.listening, item.reading, item.spokenInteraction, item.spokenProduction, item.writing);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getUserLanguagesIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
