package org.enaki.license.manager_db.persistence.sqlite.repositories

import org.enaki.license.manager_db.persistence.entities.ConnectionRequest
import org.enaki.license.manager_db.persistence.interfaces.IConnectionRequestRepository
import org.enaki.license.manager_db.persistence.sqlite.mappers.ConnectionRequestRowMapper
import org.springframework.stereotype.Repository

@Repository
class ConnectionRequestRepository: GenericRepository<ConnectionRequest>(), IConnectionRequestRepository {
    init {
        _tableName = "connection_requests"
        _rowMapper = ConnectionRequestRowMapper()
    }

    override fun add(item: ConnectionRequest): Long? {
        val script = String.format(
            "INSERT INTO %s (\"from\", \"to\") VALUES (?, ?)",
            _tableName
        )
        _jdbcTemplate.update(
            script,
            item.from,
            item.to,
        )

        val queryScript = String.format(
            "SELECT * from %s WHERE \"from\"=? AND \"to\"=?",
            _tableName
        )
        val connectionRequests: List<ConnectionRequest> = _jdbcTemplate.query(
            queryScript,
            _rowMapper,
            item.from,
            item.to
        )
        return if (connectionRequests.isEmpty()) null else connectionRequests[0].connectionRequestId
    }

    override fun delete(id: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE connection_request_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, id)
        return rowAffected == 1
    }

    override fun getByDestinationUids(from: Long, to: Long): ConnectionRequest? {
        val script = String.format("SELECT * FROM %s WHERE \"from\"=? AND \"to\"=?", _tableName)
        val connectionRequests: List<ConnectionRequest> = _jdbcTemplate.query(script, _rowMapper, from, to)
        return if (connectionRequests.isEmpty()) null else connectionRequests[0]
    }

    override fun getAllByReceiver(to: Long): List<ConnectionRequest> {
        val script = String.format("SELECT * FROM %s WHERE \"to\"=?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, to)
    }

    override fun getAllBySender(from: Long): List<ConnectionRequest> {
        val script = String.format("SELECT * FROM %s WHERE \"from\"=?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, from)
    }

    override fun getAll(): List<ConnectionRequest> {
        val script = String.format("SELECT * FROM %s", _tableName)
        return _jdbcTemplate.query(script, _rowMapper)
    }

    override fun get(id: Long): ConnectionRequest? {
        val script = String.format("SELECT * FROM %s WHERE connection_request_id = ?", _tableName)
        val connectionRequests: List<ConnectionRequest> = _jdbcTemplate.query(script, _rowMapper, id)
        return if (connectionRequests.isEmpty()) null else connectionRequests[0]
    }
}