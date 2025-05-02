package org.enaki.license.cv_db.persistence.sqlite.repositories.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Region;
import org.enaki.license.cv_db.persistence.interfaces.geography.IRegionRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.geography.RegionRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RegionRepository extends GenericRepository<Region> implements IRegionRepository {
    public RegionRepository() {
        _tableName = "regions";
        _rowMapper = new RegionRowMapper();
    }

    @Override
    public List<Region> getRegionsByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(region_name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public List<Region> getRegionsByCountryId(Long id) {
        String script = String.format("SELECT * FROM %s WHERE country_id=? ORDER BY region_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, id);
    }

    @Override
    public List<Region> getRegionsWhichHaveCitiesByCountryId(Long id) {
        String script = String.format("SELECT * FROM %s WHERE country_id=? AND region_id IN (SELECT DISTINCT(region_id) FROM cities) ORDER BY region_name", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, id);
    }

    @Override
    public List<Region> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Region get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE region_id = ?", _tableName);
        List<Region> regionList = _jdbcTemplate.query(script, _rowMapper, id);
        return regionList.isEmpty() ? null : regionList.get(0);
    }
}
