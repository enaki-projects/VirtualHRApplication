package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;
import org.enaki.license.cv_db.persistence.interfaces.cv.IPhoneNumberRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.PhoneNumberRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneNumberRepository extends GenericRepository<PhoneNumber> implements IPhoneNumberRepository {
    private final IdRowMapper _idRowMapper;

    public PhoneNumberRepository() {
        _tableName = "phone_numbers";
        _rowMapper = new PhoneNumberRowMapper();
        _idRowMapper = new IdRowMapper("phone_number_id");
    }

    @Override
    public Long add(PhoneNumber item) {
        String script = String.format("INSERT INTO %s (uid, country_id, number) VALUES (?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.countryId, item.number);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND country_id=? AND number=?", _tableName);
        List<PhoneNumber> phoneNumberList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.countryId, item.number);
        if (phoneNumberList.isEmpty())
            return null;
        else
            return phoneNumberList.get(0).phoneNumberId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE phone_number_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, PhoneNumber item) {
        String script = String.format("UPDATE %s SET uid=?, country_id=?, number=? WHERE phone_number_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.countryId, item.number, id);
        return (numberRows > 0);
    }

    @Override
    public List<PhoneNumber> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public PhoneNumber get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE phone_number_id = ?", _tableName);
        List<PhoneNumber> phoneNumberList = _jdbcTemplate.query(script, _rowMapper, id);
        return phoneNumberList.isEmpty() ? null : phoneNumberList.get(0);
    }

    @Override
    public List<PhoneNumber> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<PhoneNumber> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(title), ?) > 0 OR instr(lower(phoneNumbering_institution), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), name.toLowerCase());
    }

    @Override
    public Boolean addWithId(PhoneNumber item) {
        String script = String.format("INSERT INTO %s (phone_number_id, uid, country_id, number) VALUES (?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.phoneNumberId, item.uid, item.countryId, item.number);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getPhoneNumbersIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
