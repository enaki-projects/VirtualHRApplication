package org.enaki.license.manager_db.presentation.controllers

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCart
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCart
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.ICartService
import org.enaki.license.manager_db.presentation.utils.ControllerUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["api"])
class CartController {
    @Autowired
    private lateinit var _cartService: ICartService

    @RequestMapping(value = ["/carts"], method = [RequestMethod.GET])
    fun getCarts(request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _cartService.getAllCarts()
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/users/{uid}/carts"], method = [RequestMethod.GET])
    fun getCartsByUid(@PathVariable("uid") uid: Long, request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _cartService.getCartsByUserId(uid)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/users/{uid}/carts/with-candidate/{candidateId}"], method = [RequestMethod.GET])
    fun getCartsByUidWithCandidateStatus(@PathVariable("uid") uid: Long,
                      @PathVariable("candidateId") candidateId: Long,
                      request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _cartService.getCartsByUserIdWithCandidateBelonging(uid, candidateId)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/users/{uid}/carts/with-maintainer/{maintainerId}"], method = [RequestMethod.GET])
    fun getCartsByUidWithMaintainerStatus(@PathVariable("uid") uid: Long,
                      @PathVariable("maintainerId") maintainerId: Long,
                      request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _cartService.getCartsByUserIdWithMaintainerBelonging(uid, maintainerId)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/carts/{id}"], method = [RequestMethod.GET])
    fun getCartById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartService.getCartById(id)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/carts"], method = [RequestMethod.POST])
    fun createCart(@RequestBody cart: BusinessCreateCart, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartService.createCart(cart)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/carts/{id}"], method = [RequestMethod.PUT])
    fun replaceCart(
        @PathVariable("id") id: Long,
        @RequestBody cart: BusinessUpdateCart,
        request: HttpServletRequest
    ): ResponseEntity<*>? {
        val response: Response<*> = _cartService.updateCart(id, cart)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/carts/{id}"], method = [RequestMethod.DELETE])
    fun deleteCart(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartService.deleteCartById(id)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/carts/{id}/candidate/{candidateId}"], method = [RequestMethod.DELETE])
    fun deleteCandidateFromCart(@PathVariable("id") cartId: Long,
                                @PathVariable("candidateId") candidateId: Long,
                                request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartService.deleteItemFromCart(cartId, candidateId)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/carts/{id}/maintainer/{maintainerId}"], method = [RequestMethod.DELETE])
    fun deleteMaintainerFromCart(@PathVariable("id") cartId: Long,
                                @PathVariable("maintainerId") maintainerId: Long,
                                request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartService.deleteMaintainerFromCart(cartId, maintainerId)
        return ControllerUtils.makeResponse(response, request)
    }
}