package org.enaki.license.manager_db.persistence.sqlite.repositories

import org.enaki.license.manager_db.persistence.entities.CartMaintainer
import org.enaki.license.manager_db.persistence.interfaces.ICartMaintainerRepository
import org.enaki.license.manager_db.persistence.sqlite.mappers.CartMaintainerRowMapper
import org.enaki.license.manager_db.persistence.sqlite.mappers.IdRowMapper
import org.springframework.stereotype.Repository

@Repository
class CartMaintainerRepository: GenericRepository<CartMaintainer>(), ICartMaintainerRepository {
    init {
        _tableName = "cart_maintainers"
        _rowMapper = CartMaintainerRowMapper()
    }

    override fun getAllByCartId(cartId: Long): List<CartMaintainer> {
        val script = String.format("SELECT * FROM %s WHERE cart_id = ? ORDER BY role", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, cartId)
    }

    override fun getCartsByUidWithSpecificRole(maintainerId: Long, role: String): List<Long> {
        val script = String.format("SELECT * FROM %s WHERE maintainer_id = ? AND role = ?", _tableName)
        return _jdbcTemplate.query(script, IdRowMapper("cart_id"), maintainerId, role)
    }

    override fun add(item: CartMaintainer): Long? {
        val script = String.format(
            "INSERT INTO %s (cart_id, maintainer_id, role) VALUES (?, ?, ?)",
            _tableName
        )
        _jdbcTemplate.update(
            script,
            item.cartId,
            item.maintainerId,
            item.role,
        )

        val queryScript = String.format(
            "SELECT * from %s WHERE cart_id=? AND maintainer_id=?",
            _tableName
        )
        val cartMaintainers: List<CartMaintainer> = _jdbcTemplate.query(
            queryScript,
            _rowMapper,
            item.cartId,
            item.maintainerId
        )
        return if (cartMaintainers.isEmpty()) null else cartMaintainers[0].cartMaintainerId
    }

    override fun deleteMaintainerFromCartId(cartId: Long, maintainerId: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_id = ? AND maintainer_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, cartId, maintainerId)
        return rowAffected > 1
    }

    override fun delete(id: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_maintainer_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, id)
        return rowAffected == 1
    }

    override fun update(id: Long, item: CartMaintainer): Boolean {
        val script = String.format(
            "UPDATE %s SET role=? WHERE cart_maintainer_id=?",
            _tableName
        )
        val numberRows = _jdbcTemplate.update(
            script,
            item.role,
            id
        )
        return numberRows > 0
    }

    override fun addWithId(item: CartMaintainer): Boolean {
        val script = String.format(
            "INSERT INTO %s (cart_maintainer_id, cart_id, maintainer_id, role) VALUES (?, ?, ?, ?)",
            _tableName
        )
        val numberRows = _jdbcTemplate.update(
            script,
            item.cartMaintainerId,
            item.cartId,
            item.maintainerId,
            item.role,
        )
        return numberRows > 0
    }

    override fun getAll(): List<CartMaintainer> {
        val script = String.format("SELECT * FROM %s", _tableName)
        return _jdbcTemplate.query(script, _rowMapper)
    }

    override fun get(id: Long): CartMaintainer? {
        val script = String.format("SELECT * FROM %s WHERE cart_maintainer_id = ?", _tableName)
        val cartMaintainers: List<CartMaintainer> = _jdbcTemplate.query(script, _rowMapper, id)
        return if (cartMaintainers.isEmpty()) null else cartMaintainers[0]
    }

    override fun deleteAllByCartId(cartId: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, cartId)
        return rowAffected > 1
    }

}