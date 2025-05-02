package org.enaki.license.cv_db.persistence.sqlite.repositories.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Project;
import org.enaki.license.cv_db.persistence.interfaces.cv.IProjectRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.IdRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.cv.ProjectRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProjectRepository extends GenericRepository<Project> implements IProjectRepository {
    private final IdRowMapper _idRowMapper;

    public ProjectRepository() {
        _tableName = "projects";
        _rowMapper = new ProjectRowMapper();
        _idRowMapper = new IdRowMapper("project_id");
    }

    @Override
    public Long add(Project item) {
        String script = String.format("INSERT INTO %s (uid, name, description, from_date, to_date, url) VALUES (?, ?, ?, ?, ?, ?)", _tableName);
        _jdbcTemplate.update(script, item.uid, item.name, item.description, item.fromDate, item.toDate, item.url);

        String queryScript = String.format("SELECT * from %s WHERE uid=? AND name=? AND from_date=?", _tableName);
        List<Project> projectList =  _jdbcTemplate.query(queryScript, _rowMapper, item.uid, item.name, item.fromDate);
        if (projectList.isEmpty())
            return null;
        else
            return projectList.get(0).projectId;
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE project_id = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, Project item) {
        String script = String.format("UPDATE %s SET uid=?, name=?, description=?, from_date=?, to_date=?, url=? WHERE project_id=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.name, item.description, item.fromDate, item.toDate, item.url, id);
        return (numberRows > 0);
    }

    @Override
    public List<Project> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Project get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE project_id = ?", _tableName);
        List<Project> projectList = _jdbcTemplate.query(script, _rowMapper, id);
        return projectList.isEmpty() ? null : projectList.get(0);
    }

    @Override
    public List<Project> getAllByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, uid);
    }

    @Override
    public List<Project> getAllByName(String name) {
        String script = String.format("SELECT * FROM %s WHERE instr(lower(name), ?) > 0", _tableName);
        return _jdbcTemplate.query(script, _rowMapper, name.toLowerCase());
    }

    @Override
    public Boolean addWithId(Project item) {
        String script = String.format("INSERT INTO %s (project_id, uid, name, description, from_date, to_date, url) VALUES (?, ?, ?, ?, ?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.projectId, item.uid, item.name, item.description, item.fromDate, item.toDate, item.url);
        return (numberRows > 0);
    }

    @Override
    public List<Long> getProjectsIdsByUid(Long uid) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        return _jdbcTemplate.query(script, _idRowMapper, uid);
    }
}
