package org.enaki.license.manager_db.business.business_models.get

import org.enaki.license.manager_db.persistence.entities.Cart

class BusinessCartWithUserBelonging(var cartId: Long, var name: String, var belong: Boolean) {
    companion object{
        fun toBusinessCart(cart: Cart, belong: Boolean = false): BusinessCartWithUserBelonging {
            return BusinessCartWithUserBelonging(
                cartId = cart.cartId,
                name = cart.name,
                belong = belong
            )
        }

        fun toBusinessCartList(carts: List<Cart>, belongIds: List<Long>, excludeIds: List<Long> = emptyList()): List<BusinessCartWithUserBelonging>{
            return carts
                .filter { it.cartId !in excludeIds }
                .map { if (it.cartId in belongIds)
                toBusinessCart(it, true)
            else toBusinessCart(it)  }
        }
    }
}