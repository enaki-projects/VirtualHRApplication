package org.enaki.license.manager_db.business.services

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCartItem
import org.enaki.license.manager_db.business.business_models.get.BusinessCartItem
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCartItem
import org.enaki.license.manager_db.business.error_handling.CustomResponse
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.ICartItemService
import org.enaki.license.manager_db.persistence.entities.CartItem
import org.enaki.license.manager_db.persistence.interfaces.ICartItemRepository
import org.enaki.license.manager_db.persistence.interfaces.ICartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.stereotype.Service

@Service
class CartItemService: ICartItemService{
    @Autowired
    private lateinit var _cartRepository: ICartRepository
    @Autowired
    private lateinit var _cartItemRepository: ICartItemRepository

    override fun getAllCartItems(): Response<List<BusinessCartItem>> {
        return try {
            val cartItems: List<CartItem> = _cartItemRepository.getAll()
            Response(true, 200, BusinessCartItem.toBusinessCartItemList(cartItems))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartItemsByCartId(cartId: Long): Response<*> {
        return try {
            val cart = _cartRepository.get(cartId) ?: return CustomResponse.getItemNotFoundResponse<Unit>("Cart", cartId)

            val cartItems: List<CartItem> = _cartItemRepository.getAllByCartId(cartId)
            Response(true, 200, BusinessCartItem.toBusinessCartItemList(cartItems))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartItemsByName(name: String): Response<List<BusinessCartItem>> {
        return try {
            val cartItems: List<CartItem> = _cartItemRepository.getByName(name)
            Response(true, 200, BusinessCartItem.toBusinessCartItemList(cartItems))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartItemById(id: Long): Response<BusinessCartItem?> {
        return try {
            val cartItem = _cartItemRepository.get(id) ?: return CustomResponse.getItemNotFoundResponse("Cart Item", id)
            Response(true, 200, BusinessCartItem.toBusinessCartItem(cartItem))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteCartItemById(id: Long): Response<*> {
        return try {
            val success: Boolean = _cartItemRepository.delete(id)
            if (!success){
                return CustomResponse.getItemNotFoundResponse<Unit>("Cart Item", id)
            }
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun createCartItem(cartItem: BusinessCreateCartItem): Response<BusinessId?> {
        return try {
            val cart = _cartRepository.get(cartItem.cartId) ?: return CustomResponse.getItemNotFoundResponse("Cart", cartItem.cartId)
            val id: Long = _cartItemRepository.add(BusinessCreateCartItem.toCartItem(cartItem))
                ?: return Response(false, 400, null, "Can't create cart item")
            Response(true, 201, BusinessId(id))
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse(cartItem.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun updateCartItem(id: Long, cartItem: BusinessUpdateCartItem): Response<*> {
        return try {
            val cart = _cartRepository.get(cartItem.cartId) ?: return CustomResponse.getItemNotFoundResponse<Unit>("Cart", cartItem.cartId)

            val foundCartItem = _cartItemRepository.get(id)
            val dbCartItem: CartItem = BusinessUpdateCartItem.toCartItem(id, cartItem)
            if (foundCartItem == null) {
                val success: Boolean = _cartItemRepository.addWithId(dbCartItem)
                return Response(
                    success,
                    if (success) 201 else 500,
                    null,
                    if (success) "" else "Something Went Wrong With Cart Item Update"
                )
            }
            val success: Boolean = _cartItemRepository.update(id, BusinessUpdateCartItem.toCartItem(id, cartItem))
            Response(
                success,
                if (success) 204 else 500,
                null,
                if (success) "" else "Something Went Wrong With Cart Update"
            )
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse<Unit>(cartItem.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }
}