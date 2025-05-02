package org.enaki.license.manager_db.business.business_models.update

import org.enaki.license.manager_db.persistence.entities.CartItem

class BusinessUpdateCartItem (
    var cartItemId: Long,
    var cartId: Long,
    var candidateId: Long,
    var score: Float?,
    var notes: String?
) {

    companion object {
        fun toCartItem(cartItemId: Long, cartItem: BusinessUpdateCartItem): CartItem {
            return CartItem(
                cartItemId = cartItemId,
                cartId = cartItem.cartId,
                candidateId = cartItem.candidateId,
                score = cartItem.score,
                notes = cartItem.notes,
            )
        }
    }

    fun getDuplicatedEntityMessage(): String {
        return "Cart Item with fields (cartId, candidateId): (${cartId}, ${candidateId}) already exists."
    }
}
