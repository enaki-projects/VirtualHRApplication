package org.enaki.license.manager_db.persistence.sqlite.mappers

import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class IdRowMapper(private val idName: String = "id"): RowMapper<Long> {

    override fun mapRow(resultSet: ResultSet, rowNumber: Int): Long {
        return resultSet.getLong(idName)
    }
}