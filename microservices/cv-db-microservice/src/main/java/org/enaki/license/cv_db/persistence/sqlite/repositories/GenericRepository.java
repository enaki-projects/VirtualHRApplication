package org.enaki.license.cv_db.persistence.sqlite.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

abstract public class GenericRepository<T> {
    @Autowired
    protected JdbcTemplate _jdbcTemplate;
    protected RowMapper<T> _rowMapper;
    protected String _tableName;
}
