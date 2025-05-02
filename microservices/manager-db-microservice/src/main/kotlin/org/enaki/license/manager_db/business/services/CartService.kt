package org.enaki.license.manager_db.business.services

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCart
import org.enaki.license.manager_db.business.business_models.get.BusinessCart
import org.enaki.license.manager_db.business.business_models.get.BusinessCartWithUserBelonging
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCart
import org.enaki.license.manager_db.business.error_handling.CustomResponse
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.ICartService
import org.enaki.license.manager_db.business.utils.ServerEnvUtils
import org.enaki.license.manager_db.persistence.entities.Cart
import org.enaki.license.manager_db.persistence.entities.CartMaintainer
import org.enaki.license.manager_db.persistence.interfaces.ICartItemRepository
import org.enaki.license.manager_db.persistence.interfaces.ICartMaintainerRepository
import org.enaki.license.manager_db.persistence.interfaces.ICartRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.stereotype.Service


@Service
class CartService: ICartService {
    @Autowired
    private lateinit var _cartRepository: ICartRepository
    @Autowired
    private lateinit var _cartItemRepository: ICartItemRepository
    @Autowired
    private lateinit var _cartMaintainerRepository: ICartMaintainerRepository
    @Autowired
    private lateinit var _serverEnvUtils: ServerEnvUtils

    override fun getAllCarts(): Response<List<BusinessCart>> {
        return try {
            val carts: List<Cart> = _cartRepository.getAll()
            Response(true, 200, BusinessCart.toBusinessCartList(carts))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartsByUserId(uid: Long): Response<List<BusinessCart>> {
        return try {
            val carts: List<Cart> = _cartRepository.getByUid(uid)
            Response(true, 200, BusinessCart.toBusinessCartList(carts))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartsByUserIdWithCandidateBelonging(uid: Long, candidateId: Long): Response<List<BusinessCartWithUserBelonging>> {
        return try {
            val carts: List<Cart> = _cartRepository.getByUid(uid)
            var cartIds = _cartRepository.getByUidWithCandidateBelonging(uid, candidateId)
            Response(true, 200, BusinessCartWithUserBelonging.toBusinessCartList(carts, cartIds))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartsByUserIdWithMaintainerBelonging(uid: Long, maintainerId: Long): Response<List<BusinessCartWithUserBelonging>> {
        return try {
            val carts: List<Cart> = _cartRepository.getByUid(uid)
            var cartIds = _cartRepository.getByUidWithMaintainerBelonging(uid, maintainerId)

            var cartWithMaintainerAsCreatorIds = _cartMaintainerRepository.getCartsByUidWithSpecificRole(maintainerId, "creator")
            Response(true, 200, BusinessCartWithUserBelonging.toBusinessCartList(carts, cartIds, cartWithMaintainerAsCreatorIds))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartsByName(name: String): Response<List<BusinessCart>> {
        return try {
            val carts: List<Cart> = _cartRepository.getByName(name)
            Response(true, 200, BusinessCart.toBusinessCartList(carts))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getCartById(id: Long): Response<BusinessCart?> {
        return try {
            val cart = _cartRepository.get(id) ?: return CustomResponse.getItemNotFoundResponse("Cart", id)

            Response(true, 200, setCartReferences(cart))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteCartById(id: Long): Response<*> {
        return try {
            _cartItemRepository.deleteAllByCartId(id)
            _cartMaintainerRepository.deleteAllByCartId(id)

            val success: Boolean = _cartRepository.delete(id)
            if (!success){
                return CustomResponse.getItemNotFoundResponse<Unit>("Cart", id)
            }
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun createCart(cart: BusinessCreateCart): Response<BusinessId?> {
        return try {
            val id: Long = _cartRepository.add(BusinessCreateCart.toCart(cart))
                ?: return Response(false, 400, null, "Can't create cart")
            _cartMaintainerRepository.add(CartMaintainer(-1, id, cart.creatorId, "creator"))
            Response(true, 201, BusinessId(id))
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse(cart.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun updateCart(id: Long, cart: BusinessUpdateCart): Response<*> {
        return try {
            val foundCart = _cartRepository.get(id)
            val dbCart: Cart = BusinessUpdateCart.toCart(id, cart)
            if (foundCart == null) {
                val success: Boolean = _cartRepository.addWithId(dbCart)
                return Response(
                    success,
                    if (success) 201 else 500,
                    null,
                    if (success) "" else "Something Went Wrong With Cart Update"
                )
            }
            val success: Boolean = _cartRepository.update(id, BusinessUpdateCart.toCart(id, cart))
            Response(
                success,
                if (success) 204 else 500,
                null,
                if (success) "" else "Something Went Wrong With Cart Update"
            )
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse<Unit>(cart.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteItemFromCart(cartId: Long, candidateId: Long): Response<*> {
        return try {
            val success: Boolean = _cartItemRepository.deleteItemFromCartId(cartId, candidateId)
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteMaintainerFromCart(cartId: Long, maintainerId: Long): Response<*> {
        return try {
            val success: Boolean = _cartMaintainerRepository.deleteMaintainerFromCartId(cartId, maintainerId)
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    fun setCartReferences(cart: Cart): BusinessCart{
        val cartItems = _cartItemRepository.getAllByCartId(cart.cartId).map { it -> it.cartItemId }
        val cartMaintainers = _cartMaintainerRepository.getAllByCartId(cart.cartId).map { it -> it.cartMaintainerId }

        val businessCart = BusinessCart.toBusinessCart(cart)
        businessCart.setReferences(itemIds = cartItems,
            maintainerIds = cartMaintainers,
            serverAddress = _serverEnvUtils.serverUrlPrefix)
        return businessCart
    }
}