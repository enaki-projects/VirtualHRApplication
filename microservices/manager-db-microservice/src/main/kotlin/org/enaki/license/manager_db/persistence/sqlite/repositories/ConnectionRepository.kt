package org.enaki.license.manager_db.persistence.sqlite.repositories

import org.enaki.license.manager_db.persistence.entities.Connection
import org.enaki.license.manager_db.persistence.interfaces.IConnectionRepository
import org.enaki.license.manager_db.persistence.sqlite.mappers.ConnectionRowMapper
import org.springframework.stereotype.Repository

@Repository
class ConnectionRepository: GenericRepository<Connection>(), IConnectionRepository {
    init {
        _tableName = "connections"
        _rowMapper = ConnectionRowMapper()
    }

    override fun add(item: Connection): Long? {
        val script = String.format(
            "INSERT INTO %s (uid_1, uid_2) VALUES (?, ?)",
            _tableName
        )
        _jdbcTemplate.update(
            script,
            item.uid1,
            item.uid2,
        )

        val queryScript = String.format(
            "SELECT * from %s WHERE uid_1=? AND uid_2=?",
            _tableName
        )
        val connections: List<Connection> = _jdbcTemplate.query(
            queryScript,
            _rowMapper,
            item.uid1,
            item.uid2
        )
        return if (connections.isEmpty()) null else connections[0].connectionId
    }

    override fun delete(id: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE connection_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, id)
        return rowAffected == 1
    }

    override fun getByUids(uid1: Long, uid2: Long): Connection? {
        val script = String.format("SELECT * FROM %s WHERE (uid_1=? AND uid_2=?) OR (uid_1=? AND uid_2=?)", _tableName)
        val connections: List<Connection> = _jdbcTemplate.query(script, _rowMapper, uid1, uid2, uid2, uid1)
        return if (connections.isEmpty()) null else connections[0]
    }

    override fun getAllByUid(uid: Long): List<Connection> {
        val script = String.format("SELECT * FROM %s WHERE uid_1=? OR uid_2=?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, uid, uid)
    }

    override fun getAll(): List<Connection> {
        val script = String.format("SELECT * FROM %s", _tableName)
        return _jdbcTemplate.query(script, _rowMapper)
    }

    override fun get(id: Long): Connection? {
        val script = String.format("SELECT * FROM %s WHERE connection_id = ?", _tableName)
        val connections: List<Connection> = _jdbcTemplate.query(script, _rowMapper, id)
        return if (connections.isEmpty()) null else connections[0]
    }
}