package org.enaki.license.manager_db.business.business_models.update

import org.enaki.license.manager_db.persistence.entities.CartMaintainer

class BusinessUpdateCartMaintainer (
    var cartMaintainerId: Long,
    var cartId: Long,
    var maintainerId: Long,
    var role: String
){
    companion object {
        fun toCartMaintainer(cartMaintainerId: Long, maintainer: BusinessUpdateCartMaintainer): CartMaintainer {
            return CartMaintainer(
                cartMaintainerId = cartMaintainerId,
                cartId = maintainer.cartId,
                maintainerId = maintainer.maintainerId,
                role = maintainer.role,
            )
        }
    }


    fun getDuplicatedEntityMessage(): String {
        return "Cart Maintainer with fields (cartId, maintainerId): (${cartId}, ${maintainerId}) already exists."
    }
}