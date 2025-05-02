package org.enaki.license.manager_db.persistence.sqlite.mappers

import org.enaki.license.manager_db.persistence.entities.Cart
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CartRowMapper: RowMapper<Cart> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): Cart {
        return Cart(
                cartId = resultSet.getLong("cart_id"),
                name = resultSet.getString("name"),
                creatorId = resultSet.getLong("creator_id"),
                description = resultSet.getString("description")
            )
    }
}