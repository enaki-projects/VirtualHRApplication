package org.enaki.license.manager_db.persistence.entities

data class CartMaintainer(
    var cartMaintainerId: Long,
    var cartId: Long,
    var maintainerId: Long,
    var role: String
)
