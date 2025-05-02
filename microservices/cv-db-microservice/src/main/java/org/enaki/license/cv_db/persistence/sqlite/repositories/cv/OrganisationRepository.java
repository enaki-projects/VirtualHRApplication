package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Organisation;
import org.enaki.license.cv_db.persistence.interfaces.cv.IOrganisationRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.OrganisationRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganisationRepository extends GenericRepository<Organisation> implements IOrganisationRepository {
    public OrganisationRepository() {
        _tableName = "organisations";
        _rowMapper = new OrganisationRowMapper();

    }

    @Override
    public Long add(Organisation item) {
        String script = String.format("INSERT INTO %s (city_id, organisation_name, address) VALUES (?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.cityId, item.organisationName, item.address);

        String queryScript = String.format("SELECT * from %s WHERE city_id=? AND organisation_name=?", _tableName);
        List<Organisation> organisationList =  _jdbcTemplate.query(queryScript, _rowMapper, item.cityId, item.organisationName);
        if (organisationList.isEmpty())
            return null;
        else
            return organisationList.get(0).organisationId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE organisation_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Organisation item) {
        String script = String.format("UPDATE %s SET city_id=?, organisation_name=?, address=? WHERE organisation_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.cityId, item.organisationName, item.address, id);
        return (numberRows > 0);
    }

    @Override
    public List<Organisation> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Organisation get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE organisation_id = ?", _tableName);
        List<Organisation> organisationList = _jdbcTemplate.query(script, _rowMapper, id);
        return organisationList.isEmpty() ? null : organisationList.get(0);
    }

    @Override
    public List<Organisation> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(organisation_name), ?) > 0 OR instr(lower(address), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), name.toLowerCase());
    }

    @Override
    public Boolean addWithId(Organisation item) {
        String script = String.format("INSERT INTO %s (organisation_id, city_id, organisation_name, address)  VALUES (?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.organisationId, item.cityId, item.organisationName, item.address);
        return (numberRows > 0);
    }

    @Override
    public List<Organisation> getByCityId(Long id) {
        String script = String.format("SELECT * FROM %s WHERE city_id = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, id);
    }

    @Override
    public List<Organisation> getAllByNameAndCityId(String name, Long cityId) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(organisation_name), ?) > 0 AND  city_id = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), cityId);
    }
}
