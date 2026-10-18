package org.enaki.license.manager_db.presentation.controllers

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCartItem
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCartItem
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.ICartItemService
import org.enaki.license.manager_db.presentation.utils.ControllerUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["api/cart-items"])
class CartItemController {
    @Autowired
    private lateinit var _cartItemService: ICartItemService

    @RequestMapping(value = [""], method = [RequestMethod.GET])
    fun getCartItems(request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _cartItemService.getAllCartItems()
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/by-cart/{id}"], method = [RequestMethod.GET])
    fun getCartItemsByCartId(@PathVariable("id") cartId: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartItemService.getCartItemsByCartId(cartId)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getCartItemById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartItemService.getCartItemById(id)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = [""], method = [RequestMethod.POST])
    fun createCartItem(@RequestBody cartItem: BusinessCreateCartItem, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartItemService.createCartItem(cartItem)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
    fun replaceCartItem(
        @PathVariable("id") id: Long,
        @RequestBody cartItem: BusinessUpdateCartItem,
        request: HttpServletRequest
    ): ResponseEntity<*>? {
        val response: Response<*> = _cartItemService.updateCartItem(id, cartItem)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun deleteCartItem(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartItemService.deleteCartItemById(id)
        return ControllerUtils.makeResponse(response, request)
    }
}