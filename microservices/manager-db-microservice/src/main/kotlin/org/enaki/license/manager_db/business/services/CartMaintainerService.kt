package org.enaki.license.manager_db.business.services

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCartMaintainer
import org.enaki.license.manager_db.business.business_models.get.BusinessCartMaintainer
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCartMaintainer
import org.enaki.license.manager_db.business.error_handling.CustomResponse
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.ICartMaintainerService
import org.enaki.license.manager_db.persistence.entities.CartMaintainer
import org.enaki.license.manager_db.persistence.interfaces.ICartMaintainerRepository
import org.enaki.license.manager_db.persistence.interfaces.ICartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.stereotype.Service

@Service
class CartMaintainerService: ICartMaintainerService {
    @Autowired
    private lateinit var _cartRepository: ICartRepository
    @Autowired
    private lateinit var _cartMaintainerRepository: ICartMaintainerRepository

    override fun getAllCartMaintainers(): Response<List<BusinessCartMaintainer>> {
        return try {
            val cartMaintainers: List<CartMaintainer> = _cartMaintainerRepository.getAll()
            Response(true, 200, BusinessCartMaintainer.toBusinessCartMaintainerList(cartMaintainers))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartMaintainersByCartId(cartId: Long): Response<*> {
        return try {
            val cart = _cartRepository.get(cartId) ?: return CustomResponse.getItemNotFoundResponse<Unit>("Cart", cartId)

            val cartMaintainers: List<CartMaintainer> = _cartMaintainerRepository.getAllByCartId(cartId)
            Response(true, 200, BusinessCartMaintainer.toBusinessCartMaintainerList(cartMaintainers))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartMaintainerById(id: Long): Response<BusinessCartMaintainer?> {
        return try {
            val cartMaintainer = _cartMaintainerRepository.get(id) ?: return CustomResponse.getItemNotFoundResponse("Cart Maintainer", id)
            Response(true, 200, BusinessCartMaintainer.toBusinessCartMaintainer(cartMaintainer))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteCartMaintainerById(id: Long): Response<*> {
        return try {
            val success: Boolean = _cartMaintainerRepository.delete(id)
            if (!success){
                return CustomResponse.getItemNotFoundResponse<Unit>("Cart Maintainer", id)
            }
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun createCartMaintainer(cartMaintainer: BusinessCreateCartMaintainer): Response<BusinessId?> {
        return try {
            val cart = _cartRepository.get(cartMaintainer.cartId) ?: return CustomResponse.getItemNotFoundResponse("Cart", cartMaintainer.cartId)
            val id: Long = _cartMaintainerRepository.add(BusinessCreateCartMaintainer.toCartMaintainer(cartMaintainer))
                ?: return Response(false, 400, null, "Can't create cart maintainer")
            Response(true, 201, BusinessId(id))
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse(cartMaintainer.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun updateCartMaintainer(id: Long, cartMaintainer: BusinessUpdateCartMaintainer): Response<*> {
        return try {
            val cart = _cartRepository.get(cartMaintainer.cartId) ?: return CustomResponse.getItemNotFoundResponse<Unit>("Cart", cartMaintainer.cartId)

            val foundCartMaintainer = _cartMaintainerRepository.get(id)
            val dbCartMaintainer: CartMaintainer = BusinessUpdateCartMaintainer.toCartMaintainer(id, cartMaintainer)
            if (foundCartMaintainer == null) {
                val success: Boolean = _cartMaintainerRepository.addWithId(dbCartMaintainer)
                return Response(
                    success,
                    if (success) 201 else 500,
                    null,
                    if (success) "" else "Something Went Wrong With Cart Maintainer Update"
                )
            }
            val success: Boolean = _cartMaintainerRepository.update(id, BusinessUpdateCartMaintainer.toCartMaintainer(id, cartMaintainer))
            Response(
                success,
                if (success) 204 else 500,
                null,
                if (success) "" else "Something Went Wrong With Cart Maintainer Update"
            )
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse<Unit>(cartMaintainer.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }
}