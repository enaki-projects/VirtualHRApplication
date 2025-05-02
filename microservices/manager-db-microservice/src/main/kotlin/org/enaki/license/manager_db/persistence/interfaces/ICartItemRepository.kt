package org.enaki.license.manager_db.persistence.interfaces

import org.enaki.license.manager_db.persistence.entities.CartItem
import org.enaki.license.manager_db.persistence.interfaces.generic.IRepository

interface ICartItemRepository: IRepository<CartItem> {
    fun getAllByCartId(cartId: Long): List<CartItem>
    fun getByName(name: String): List<CartItem>
    fun deleteAllByCartId(cartId: Long): Boolean
    fun deleteItemFromCartId(cartId: Long, candidateId: Long): Boolean
}