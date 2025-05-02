package org.enaki.license.manager_db.business.interfaces

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCartItem
import org.enaki.license.manager_db.business.business_models.get.BusinessCartItem
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCartItem
import org.enaki.license.manager_db.business.error_handling.Response

interface ICartItemService {
    fun getAllCartItems(): Response<List<BusinessCartItem>>
    fun getCartItemsByCartId(cartId: Long): Response<*>
    fun getCartItemsByName(name: String): Response<List<BusinessCartItem>>

    fun getCartItemById(id: Long): Response<BusinessCartItem?>
    fun deleteCartItemById(id: Long): Response<*>
    fun createCartItem(cartItem: BusinessCreateCartItem): Response<BusinessId?>
    fun updateCartItem(id: Long, cartItem: BusinessUpdateCartItem): Response<*>
}