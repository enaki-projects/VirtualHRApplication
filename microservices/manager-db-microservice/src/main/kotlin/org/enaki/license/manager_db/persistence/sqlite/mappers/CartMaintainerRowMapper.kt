package org.enaki.license.manager_db.persistence.sqlite.mappers

import org.enaki.license.manager_db.persistence.entities.CartMaintainer
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class CartMaintainerRowMapper: RowMapper<CartMaintainer> {
    override fun mapRow(resultSet: ResultSet, rowNumber: Int): CartMaintainer {
        return CartMaintainer(
            cartMaintainerId = resultSet.getLong("cart_maintainer_id"),
            cartId = resultSet.getLong("cart_id"),
            maintainerId = resultSet.getLong("maintainer_id"),
            role = resultSet.getString("role")
        )
    }

}
