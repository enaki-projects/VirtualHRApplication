package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Location;
import org.enaki.license.cv_db.persistence.interfaces.cv.ILocationRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.LocationRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationRepository extends GenericRepository<Location> implements ILocationRepository {
    private final IdRowMapper _idRowMapper;

    public LocationRepository() {
        _tableName = "locations";
        _rowMapper = new LocationRowMapper();
        _idRowMapper = new IdRowMapper("location_id");
    }

    @Override
    public Long add(Location item) {
        String script = String.format("INSERT INTO %s (uid, city_id, type, address) VALUES (?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.cityId, item.type, item.address);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND city_id=? AND type=? AND address=?", _tableName);
        List<Location> locationList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.cityId, item.type, item.address);
        if (locationList.isEmpty())
            return null;
        else
            return locationList.get(0).locationId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE location_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Location item) {
        String script = String.format("UPDATE %s SET uid=?, city_id=?, type=?, address=? WHERE location_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.cityId, item.type, item.address, id);
        return (numberRows > 0);
    }

    @Override
    public List<Location> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Location get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE location_id = ?", _tableName);
        List<Location> locationList = _jdbcTemplate.query(script, _rowMapper, id);
        return locationList.isEmpty() ? null : locationList.get(0);
    }

    @Override
    public List<Location> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<Location> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(address), ?) > 0 OR instr(lower(type), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase(), name.toLowerCase());
    }

    @Override
    public Boolean addWithId(Location item) {
        String script = String.format("INSERT INTO %s (location_id, uid, city_id, type, address) VALUES (?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.locationId, item.uid, item.cityId, item.type, item.address);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getLocationsIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
