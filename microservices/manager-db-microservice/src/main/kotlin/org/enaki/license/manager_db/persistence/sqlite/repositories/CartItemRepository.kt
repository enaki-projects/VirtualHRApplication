package org.enaki.license.manager_db.persistence.sqlite.repositories

import org.enaki.license.manager_db.persistence.entities.CartItem
import org.enaki.license.manager_db.persistence.interfaces.ICartItemRepository
import org.enaki.license.manager_db.persistence.sqlite.mappers.CartItemRowNumber
import org.springframework.stereotype.Repository

@Repository
class CartItemRepository: GenericRepository<CartItem>(), ICartItemRepository {
    init {
        _tableName = "cart_items"
        _rowMapper = CartItemRowNumber()
    }

    override fun getAllByCartId(cartId: Long): List<CartItem> {
        val script = String.format("SELECT * FROM %s WHERE cart_id = ?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, cartId)
    }

    override fun getByName(name: String): List<CartItem> {
        val script = String.format("SELECT * FROM %s WHERE name = ?", _tableName)
        return _jdbcTemplate.query(script, _rowMapper, name)
    }

    override fun deleteAllByCartId(cartId: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, cartId)
        return rowAffected > 1
    }

    override fun deleteItemFromCartId(cartId: Long, candidateId: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_id = ? AND candidate_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, cartId, candidateId)
        return rowAffected > 1
    }

    override fun add(item: CartItem): Long? {
        val script = String.format(
            "INSERT INTO %s (cart_id, candidate_id, score, notes) VALUES (?, ?, ?, ?)",
            _tableName
        )
        _jdbcTemplate.update(
            script,
            item.cartId,
            item.candidateId,
            item.score,
            item.notes
        )

        val queryScript = String.format(
            "SELECT * from %s WHERE cart_id=? AND candidate_id=?",
            _tableName
        )
        val cartItems: List<CartItem> = _jdbcTemplate.query(
            queryScript,
            _rowMapper,
            item.cartId,
            item.candidateId
        )
        return if (cartItems.isEmpty()) null else cartItems[0].cartItemId
    }

    override fun delete(id: Long): Boolean {
        val script = String.format("DELETE FROM %s WHERE cart_item_id = ?", _tableName)
        val rowAffected = _jdbcTemplate.update(script, id)
        return rowAffected == 1
    }

    override fun update(id: Long, item: CartItem): Boolean {
        val script = String.format(
            "UPDATE %s SET cart_id=?, candidate_id=?, score=?, notes=? WHERE cart_item_id=?",
            _tableName
        )
        val numberRows = _jdbcTemplate.update(
            script,
            item.cartId,
            item.candidateId,
            item.score,
            item.notes,
            id
        )
        return numberRows > 0
    }

    override fun addWithId(item: CartItem): Boolean {
        val script = String.format(
            "INSERT INTO %s (cart_item_id, cart_id, candidate_id, score, notes) VALUES (?, ?, ?, ?, ?)",
            _tableName
        )
        val numberRows = _jdbcTemplate.update(
            script,
            item.cartItemId,
            item.cartId,
            item.candidateId,
            item.score,
            item.notes
        )
        return numberRows > 0
    }

    override fun getAll(): List<CartItem> {
        val script = String.format("SELECT * FROM %s", _tableName)
        return _jdbcTemplate.query(script, _rowMapper)
    }

    override fun get(id: Long): CartItem? {
        val script = String.format("SELECT * FROM %s WHERE cart_item_id = ?", _tableName)
        val cartItems: List<CartItem> = _jdbcTemplate.query(script, _rowMapper, id)
        return if (cartItems.isEmpty()) null else cartItems[0]
    }
}