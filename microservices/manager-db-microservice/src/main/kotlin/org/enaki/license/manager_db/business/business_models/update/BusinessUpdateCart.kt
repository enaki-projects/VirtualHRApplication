package org.enaki.license.manager_db.business.business_models.update

import org.enaki.license.manager_db.persistence.entities.Cart

class BusinessUpdateCart(var cartId: Long, var name: String, var creatorId: Long, var description: String?) {

    companion object{
        fun toCart(cartId: Long, cart: BusinessUpdateCart): Cart {
            return Cart(
                cartId = cartId,
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