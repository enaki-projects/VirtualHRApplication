package org.enaki.license.manager_db.persistence.sqlite.mappers

import org.enaki.license.manager_db.persistence.entities.ConnectionRequest
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class ConnectionRequestRowMapper: RowMapper<ConnectionRequest> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): ConnectionRequest {
        return ConnectionRequest(
            connectionRequestId = resultSet.getLong("connection_request_id"),
            from = resultSet.getLong("from"),
            to = resultSet.getLong("to"),
        )
    }
}