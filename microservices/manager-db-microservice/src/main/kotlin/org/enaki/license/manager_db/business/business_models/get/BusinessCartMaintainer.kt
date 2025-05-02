package org.enaki.license.manager_db.business.business_models.get

import org.enaki.license.manager_db.persistence.entities.CartMaintainer

class BusinessCartMaintainer (
    var cartMaintainerId: Long,
    var cartId: Long,
    var maintainerId: Long,
    var role: String
){
    companion object{
        fun toBusinessCartMaintainer(maintainer: CartMaintainer): BusinessCartMaintainer {
            return BusinessCartMaintainer(
                cartMaintainerId = maintainer.cartMaintainerId,
                cartId = maintainer.cartId,
                maintainerId = maintainer.maintainerId,
                role = maintainer.role,
            )
        }

        fun toBusinessCartMaintainerList(carts: List<CartMaintainer>): List<BusinessCartMaintainer>{
            return carts.map { BusinessCartMaintainer.toBusinessCartMaintainer(it) }
        }
    }
}