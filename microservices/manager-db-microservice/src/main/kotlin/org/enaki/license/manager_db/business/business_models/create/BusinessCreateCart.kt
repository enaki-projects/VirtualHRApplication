package org.enaki.license.manager_db.business.business_models.create

import org.enaki.license.manager_db.persistence.entities.Cart

class BusinessCreateCart(var cartId: Long, var name: String, var creatorId: Long, var description: String?) {

    companion object{
        fun toCart(cart: BusinessCreateCart): Cart {
            return Cart(
                cartId = cart.cartId,
                name = cart.name,
                creatorId = cart.creatorId,
                description = cart.description,
            )
        }
    }

    fun getDuplicatedEntityMessage(): String {
        return "Cart with fields (name, creatorId): (${name}, ${creatorId}) already exists."
    }
}