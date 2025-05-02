package org.enaki.license.cv_db.persistence.sqlite.repositories.geography;

import org.enaki.license.cv_db.persistence.entities.geography.City;
import org.enaki.license.cv_db.persistence.interfaces.geography.ICityRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.geography.CityRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CityRepository extends GenericRepository<City> implements ICityRepository {
    public CityRepository() {
        _tableName = "cities";
        _rowMapper = new CityRowMapper();
    }

    @Override
    public List<City> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public City get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE city_id = ?", _tableName);
        List<City> cityList = _jdbcTemplate.query(script, _rowMapper, id);
        return cityList.isEmpty() ? null : cityList.get(0);
    }

    @Override
    public List<City> getCitiesByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(city_name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public List<City> getCitiesByCountryId(Long countryId) {
        String script = String.format("SELECT city_id, c.region_id region_id, city_name, latitude, longitude FROM %s c, regions r " +
                "WHERE c.region_id = r.region_id AND r.country_id = ? ORDER BY city_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, countryId);
    }

    @Override
    public List<City> getCitiesByRegionId(Long regionId) {
        String script = String.format("SELECT * FROM %s WHERE region_id = ? ORDER BY city_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, regionId);
    }
}
