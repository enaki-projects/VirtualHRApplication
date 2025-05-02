package org.enaki.license.manager_db.presentation.controllers

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateConnection
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.IConnectionService
import org.enaki.license.manager_db.presentation.utils.ControllerUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["api/"])
class ConnectionController {
    @Autowired
    private lateinit var _connectionService: IConnectionService

    @RequestMapping(value = ["connections"], method = [RequestMethod.GET])
    fun getConnections(request: HttpServletRequest): ResponseEntity<*> {
        val response: Response<*> = _connectionService.getAllConnections()
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connections/{id}"], method = [RequestMethod.GET])
    fun getConnectionById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionService.getConnectionById(id)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["user/{uid}/connections"], method = [RequestMethod.GET])
    fun getConnectionByUid(@PathVariable("uid") uid: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionService.getConnectionsByUserId(uid)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connections/"], method = [RequestMethod.GET])
    fun getConnectionByUids(@RequestParam(required = true) uid1: Long,
                            @RequestParam(required = true) uid2: Long,
                            request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionService.getConnectionByUids(uid1, uid2)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connections"], method = [RequestMethod.POST])
    fun createConnection(@RequestBody connection: BusinessCreateConnection, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionService.createConnection(connection)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connections/{id}"], method = [RequestMethod.DELETE])
    fun deleteConnection(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionService.deleteConnectionById(id)
        return ControllerUtils.makeResponse(response, request)
    }
}
