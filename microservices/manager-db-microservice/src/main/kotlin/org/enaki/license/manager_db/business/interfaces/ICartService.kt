package org.enaki.license.manager_db.business.interfaces

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCart
import org.enaki.license.manager_db.business.business_models.get.BusinessCart
import org.enaki.license.manager_db.business.business_models.get.BusinessCartWithUserBelonging
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCart
import org.enaki.license.manager_db.business.error_handling.Response

interface ICartService {
    fun getAllCarts(): Response<List<BusinessCart>>
    fun getCartsByUserId(uid: Long): Response<List<BusinessCart>>
    fun getCartsByName(name: String): Response<List<BusinessCart>>
    fun getCartsByUserIdWithCandidateBelonging(uid: Long, candidateId: Long): Response<List<BusinessCartWithUserBelonging>>
    fun getCartsByUserIdWithMaintainerBelonging(uid: Long, maintainerId: Long): Response<List<BusinessCartWithUserBelonging>>

    fun getCartById(id: Long): Response<BusinessCart?>
    fun createCart(cart: BusinessCreateCart): Response<BusinessId?>
    fun updateCart(id: Long, cart: BusinessUpdateCart): Response<*>
    fun deleteItemFromCart(cartId: Long, candidateId: Long): Response<*>
    fun deleteMaintainerFromCart(cartId: Long, maintainerId: Long): Response<*>
    fun deleteCartById(id: Long): Response<*>
}