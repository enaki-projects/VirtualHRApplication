package org.enaki.license.manager_db.persistence.sqlite.mappers

import org.enaki.license.manager_db.persistence.entities.Connection
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ConnectionRowMapper: RowMapper<Connection> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): Connection? {
        return Connection(
            connectionId = resultSet.getLong("connection_id"),
            uid1 = resultSet.getLong("uid_1"),
            uid2 = resultSet.getLong("uid_2")
        )
    }
}