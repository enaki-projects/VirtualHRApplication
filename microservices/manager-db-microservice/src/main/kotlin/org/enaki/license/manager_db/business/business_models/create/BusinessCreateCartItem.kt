package org.enaki.license.manager_db.business.business_models.create

import org.enaki.license.manager_db.persistence.entities.CartItem

class BusinessCreateCartItem (
    var cartItemId: Long,
    var cartId: Long,
    var candidateId: Long,
    var score: Float?,
    var notes: String?
){
    fun getDuplicatedEntityMessage(): String {
        return "Cart Item with fields (cartId, candidateId): (${cartId}, ${candidateId}) already exists."
    }

    companion object{
        fun toCartItem(cartItem: BusinessCreateCartItem): CartItem {
            return CartItem(
                cartItemId = cartItem.cartItemId,
                cartId = cartItem.cartId,
                candidateId = cartItem.candidateId,
                score = cartItem.score,
                notes = cartItem.notes,
            )
        }
    }
}