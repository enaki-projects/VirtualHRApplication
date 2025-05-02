package org.enaki.license.manager_db.persistence.sqlite.repositories

import org.enaki.license.manager_db.persistence.entities.Cart
import org.enaki.license.manager_db.persistence.interfaces.ICartRepository
import org.enaki.license.manager_db.persistence.sqlite.mappers.CartRowMapper
import org.enaki.license.manager_db.persistence.sqlite.mappers.IdRowMapper
import org.springframework.stereotype.Repository

@Repository
class CartRepository: GenericRepository<Cart>(), ICartRepository {
    init {
        _tableName = "carts"
        _rowMapper = CartRowMapper()
    }

    override fun getByName(name: String): List<Cart> {
        val script = String.format("SELECT * FROM %s WHERE name=?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, name)
    }

    override fun getByUid(uid: Long): List<Cart> {
        val script = String.format("SELECT * FROM %s c, cart_maintainers m WHERE c.cart_id=m.cart_id AND m.maintainer_id = ?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, uid)
    }

    override fun getByUidWithCandidateBelonging(uid: Long, candidateId: Long): List<Long> {
        val script = String.format("SELECT c.cart_id FROM %s c, cart_maintainers m, cart_items ci WHERE c.cart_id=m.cart_id AND m.maintainer_id = ? AND ci.cart_id = c.cart_id AND ci.candidate_id = ?", _tableName)
        return _jdbcTemplate.query(script, IdRowMapper("cart_id"), uid, candidateId)
    }

    override fun getByUidWithMaintainerBelonging(uid: Long, maintainerId: Long): List<Long> {
        val script = String.format("SELECT c.cart_id FROM %s c, cart_maintainers m, cart_maintainers mi WHERE c.cart_id=m.cart_id AND m.maintainer_id = ? AND mi.cart_id = c.cart_id AND mi.maintainer_id = ? AND mi.role != 'creator'", _tableName)
        return _jdbcTemplate.query(script, IdRowMapper("cart_id"), uid, maintainerId)
    }

    override fun add(item: Cart): Long? {
        val script = String.format(
            "INSERT INTO %s (name, creator_id, description) VALUES (?, ?, ?)",
            _tableName
        )
        _jdbcTemplate.update(
            script,
            item.name,
            item.creatorId,
            item.description,
        )

        val queryScript = String.format(
            "SELECT * from %s WHERE name=? AND creator_id=?",
            _tableName
        )
        val carts: List<Cart> = _jdbcTemplate.query(
            queryScript,
            _rowMapper,
            item.name,
            item.creatorId
        )
        return if (carts.isEmpty()) null else carts[0].cartId
    }

    override fun delete(id: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, id)
        return rowAffected == 1
    }

    override fun update(id: Long, item: Cart): Boolean {
        val script = String.format(
            "UPDATE %s SET name=?, description=? WHERE cart_id=?",
            _tableName
        )
        val numberRows = _jdbcTemplate.update(
            script,
            item.name,
            item.description,
            id
        )
        return numberRows > 0
    }

    override fun addWithId(item: Cart): Boolean {
        val script = String.format(
            "INSERT INTO %s (cart_id, name, creator_id, description) VALUES (?, ?, ?, ?)",
            _tableName
        )
        val numberRows = _jdbcTemplate.update(
            script,
            item.cartId,
            item.name,
            item.creatorId,
            item.description,
        )
        return numberRows > 0
    }

    override fun getAll(): List<Cart> {
        val script = String.format("SELECT * FROM %s", _tableName)
        return _jdbcTemplate.query(script, _rowMapper)
    }

    override fun get(id: Long): Cart? {
        val script = String.format("SELECT * FROM %s WHERE cart_id = ?", _tableName)
        val carts: List<Cart> = _jdbcTemplate.query(script, _rowMapper, id)
        return if (carts.isEmpty()) null else carts[0]
    }
}