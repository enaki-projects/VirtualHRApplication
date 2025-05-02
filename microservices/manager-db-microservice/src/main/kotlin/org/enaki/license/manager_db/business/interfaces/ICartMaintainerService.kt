package org.enaki.license.manager_db.business.interfaces

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCartMaintainer
import org.enaki.license.manager_db.business.business_models.get.BusinessCartMaintainer
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCartMaintainer
import org.enaki.license.manager_db.business.error_handling.Response

interface ICartMaintainerService {
    fun getAllCartMaintainers(): Response<List<BusinessCartMaintainer>>
    fun getCartMaintainersByCartId(cartId: Long): Response<*>

    fun getCartMaintainerById(id: Long): Response<BusinessCartMaintainer?>
    fun deleteCartMaintainerById(id: Long): Response<*>
    fun createCartMaintainer(cartMaintainer: BusinessCreateCartMaintainer): Response<BusinessId?>
    fun updateCartMaintainer(id: Long, cartMaintainer: BusinessUpdateCartMaintainer): Response<*>
}