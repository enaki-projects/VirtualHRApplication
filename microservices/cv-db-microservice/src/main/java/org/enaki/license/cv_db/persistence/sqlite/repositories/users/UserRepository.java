package org.enaki.license.cv_db.persistence.sqlite.repositories.users;

import org.enaki.license.cv_db.persistence.entities.users.User;
import org.enaki.license.cv_db.persistence.interfaces.users.IUserRepository;
import org.enaki.license.cv_db.persistence.sqlite.mappers.users.UserRoleRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.mappers.users.UserRowMapper;
import org.enaki.license.cv_db.persistence.sqlite.repositories.GenericRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends GenericRepository<User> implements IUserRepository  {

    private final RowMapper<Long> _userRoleMapper;

    public UserRepository() {
        _rowMapper = new UserRowMapper();
        _userRoleMapper = new UserRoleRowMapper();
        _tableName = "users";
    }

    @Override
    public List<User> getAll() {
        String script = String.format("SELECT * FROM %s", _tableName);
        return _jdbcTemplate.query(script, _rowMapper);
    }

    @Override
    public Long add(User item) {
        String script = String.format("INSERT INTO %s (email, password) VALUES (?, ?)", _tableName);
        _jdbcTemplate.update(script, item.email, item.password);

        String queryScript = String.format("SELECT * from %s WHERE email=?", _tableName);
        List<User> userList =  _jdbcTemplate.query(queryScript, _rowMapper, item.email);
        if (userList.isEmpty())
            return null;
        else
            return userList.get(0).uid;
    }

    @Override
    public List<Long> getUserRolesIds(Long uid) {
        String script = String.format("SELECT role_id FROM user_roles WHERE uid=?", _tableName);
        return _jdbcTemplate.query(script, _userRoleMapper, uid);
    }

    @Override
    public Boolean grantRole(Long uid, Long roleId) {
        String script = String.format("INSERT INTO user_roles (uid, role_id) VALUES (?, ?)", _tableName);
        _jdbcTemplate.update(script, uid, roleId);
        return true;
    }

    @Override
    public Boolean revokeRole(Long uid, Long roleId) {
        String script = String.format("DELETE FROM user_roles WHERE uid=? AND role_id=?", _tableName);
        _jdbcTemplate.update(script, uid, roleId);
        return true;
    }

    @Override
    public User get(Long id) {
        String script = String.format("SELECT * FROM %s WHERE uid = ?", _tableName);
        List<User> userList = _jdbcTemplate.query(script, _rowMapper, id);
        return userList.isEmpty() ? null : userList.get(0);
    }

    @Override
    public Boolean delete(Long id) {
        String script = String.format("DELETE FROM %s WHERE uid = ?", _tableName);
        int rowAffected = _jdbcTemplate.update(script, id);
        return (rowAffected == 1);
    }

    @Override
    public Boolean update(Long id, User item) {
        String script = String.format("UPDATE %s SET email=?, password=? WHERE uid=?", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.email, item.password, id);
        return (numberRows > 0);
    }

    @Override
    public User getByEmail(String email) {
        String script = String.format("SELECT * FROM %s WHERE email = ?", _tableName);
        List<User> userList = _jdbcTemplate.query(script, _rowMapper, email);
        return userList.isEmpty() ? null : userList.get(0);
    }

    @Override
    public Boolean addWithId(User item) {
        String script = String.format("INSERT INTO %s (uid, email, password) VALUES (?, ?, ?)", _tableName);
        int numberRows = _jdbcTemplate.update(script, item.uid, item.email, item.password);
        return (numberRows > 0);
    }

    @Override
    public User getByCredentials(String email, String password) {
        String script = String.format("SELECT * FROM %s WHERE email = ? AND password = ?", _tableName);
        List<User> userList = _jdbcTemplate.query(script, _rowMapper, email, password);
        return userList.isEmpty() ? null : userList.get(0);
    }
}