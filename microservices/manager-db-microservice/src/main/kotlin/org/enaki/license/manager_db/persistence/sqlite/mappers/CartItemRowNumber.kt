package org.enaki.license.manager_db.persistence.sqlite.mappers

import org.enaki.license.manager_db.persistence.entities.CartItem
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CartItemRowNumber: RowMapper<CartItem> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): CartItem {
        return CartItem(
            cartItemId = resultSet.getLong("cart_item_id"),
            cartId = resultSet.getLong("cart_id"),
            candidateId = resultSet.getLong("candidate_id"),
            score = resultSet.getFloat("score"),
            notes = resultSet.getString("notes")
        )
    }
}