package org.enaki.license.manager_db.business.business_models.get

import org.enaki.license.manager_db.persistence.entities.Cart

class BusinessCart(var cartId: Long, var name: String, var creatorId: Long, var description: String?) {
    var references= hashMapOf<String, List<String>>(
        "maintainers" to mutableListOf(),
        "items" to mutableListOf()
    )

    companion object{
        fun toBusinessCart(cart: Cart): BusinessCart {
            return BusinessCart(
                cartId = cart.cartId,
                creatorId = cart.creatorId,
                name = cart.name,
                description = cart.description,
            )
        }

        fun toBusinessCartList(carts: List<Cart>): List<BusinessCart>{
            return carts.map { BusinessCart.toBusinessCart(it) }
        }
    }

    fun setReferences(maintainerIds: List<Long>, itemIds: List<Long>, serverAddress: String){
        references["maintainers"] = maintainerIds.map { it -> "${serverAddress}/api/cart-maintainers/${it}" }
        references["items"] = itemIds.map { it -> "${serverAddress}/api/cart-items/${it}" }
    }
}