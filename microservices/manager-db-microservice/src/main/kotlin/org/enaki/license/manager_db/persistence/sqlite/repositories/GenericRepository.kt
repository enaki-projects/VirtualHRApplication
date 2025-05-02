package org.enaki.license.manager_db.persistence.sqlite.repositories

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper

abstract class GenericRepository<T> {
    @Autowired
    protected lateinit var _jdbcTemplate: JdbcTemplate
    protected lateinit var _rowMapper: RowMapper<T>
    protected lateinit var _tableName: String
}