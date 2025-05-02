package org.enaki.license.manager_db.business.business_models.get

import org.enaki.license.manager_db.persistence.entities.CartItem

class BusinessCartItem(
    var cartItemId: Long,
    var cartId: Long,
    var candidateId: Long,
    var score: Float?,
    var notes: String?
){
    companion object {
        fun toBusinessCartItem(cartItem: CartItem): BusinessCartItem{
            return BusinessCartItem(
                cartItemId = cartItem.cartItemId,
                cartId = cartItem.cartId,
                candidateId = cartItem.candidateId,
                score = cartItem.score,
                notes = cartItem.notes,
            )
        }

        fun toBusinessCartItemList(cartItems: List<CartItem>): List<BusinessCartItem>{
            return cartItems.map { toBusinessCartItem(it) }
        }
    }
}