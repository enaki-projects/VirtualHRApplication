package org.enaki.license.cv_db.persistence.sqlite.repositories.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Country;
import org.enaki.license.cv_db.persistence.interfaces.geography.ICountryRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.geography.CountryRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CountryRepository extends GenericRepository<Country> implements ICountryRepository {
    public CountryRepository() {
        _tableName = "countries";
        _rowMapper = new CountryRowMapper();
    }

    @Override
    public List<Country> getAll() {
        String script = String.format("SELECT * FROM %s ORDER BY country_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public List<Country> getCountriesWhichHaveRegions() {
        String script = String.format("SELECT * FROM %s WHERE country_id IN (SELECT DISTINCT(country_id) FROM regions) ORDER BY country_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public List<Country> getCountriesWhichHaveCities() {
        String script = String.format("SELECT * FROM %s WHERE country_id IN (SELECT DISTINCT(country_id) FROM regions WHERE region_id IN (SELECT DISTINCT(region_id) FROM cities)) ORDER BY country_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public List<Country> getCountriesWhichHaveUniversities() {
        String script = String.format("SELECT * FROM %s WHERE country_id IN (SELECT DISTINCT(country_id) FROM educational_institutions) ORDER BY country_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Country get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE country_id = ?", _tableName);
        List<Country> countryList = _jdbcTemplate.query(script, _rowMapper, id);
        return countryList.isEmpty() ? null : countryList.get(0);
    }

    @Override
    public List<Country> getCountriesByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(country_name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }
}
