package org.enaki.license.manager_db.presentation.controllers

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateCartMaintainer
import org.enaki.license.manager_db.business.business_models.update.BusinessUpdateCartMaintainer
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.ICartMaintainerService
import org.enaki.license.manager_db.presentation.utils.ControllerUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["api/cart-maintainers"])
class CartMaintainerController {
    @Autowired
    private lateinit var _cartMaintainerService: ICartMaintainerService

    @RequestMapping(value = [""], method = [RequestMethod.GET])
    fun getCartMaintainers(request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _cartMaintainerService.getAllCartMaintainers()
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/by-cart/{id}"], method = [RequestMethod.GET])
    fun getCartMaintainersByCartId(@PathVariable("id") cartId: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartMaintainerService.getCartMaintainersByCartId(cartId)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.GET])
    fun getCartMaintainerById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartMaintainerService.getCartMaintainerById(id)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = [""], method = [RequestMethod.POST])
    fun createCartMaintainer(@RequestBody cartMaintainer: BusinessCreateCartMaintainer, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartMaintainerService.createCartMaintainer(cartMaintainer)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.PUT])
    fun replaceCartMaintainer(
        @PathVariable("id") id: Long,
        @RequestBody cartMaintainer: BusinessUpdateCartMaintainer,
        request: HttpServletRequest
    ): ResponseEntity<*>? {
        val response: Response<*> = _cartMaintainerService.updateCartMaintainer(id, cartMaintainer)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    fun deleteCartMaintainer(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _cartMaintainerService.deleteCartMaintainerById(id)
        return ControllerUtils.makeResponse(response, request)
    }
}