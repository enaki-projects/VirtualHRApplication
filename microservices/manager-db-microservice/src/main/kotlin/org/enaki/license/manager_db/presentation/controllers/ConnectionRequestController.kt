package org.enaki.license.manager_db.presentation.controllers

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateConnectionRequest
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.IConnectionRequestService
import org.enaki.license.manager_db.presentation.utils.ControllerUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping(value = ["api/"])
class ConnectionRequestRequestController {
    @Autowired
    private lateinit var _connectionRequestService: IConnectionRequestService

    @RequestMapping(value = ["connection-requests"], method = [RequestMethod.GET])
    fun getConnectionRequests(
            @RequestParam(required = false, defaultValue = "-1") from: Long,
            @RequestParam(required = false, defaultValue = "-1") to: Long,
            request: HttpServletRequest): ResponseEntity<*>
    {
        val response: Response<*> = when {
            from != -1L && to != -1L -> _connectionRequestService.getConnectionRequestByDestinationUids(from, to)
            from != -1L -> _connectionRequestService.getConnectionRequestsBySenderUid(from)
            to != -1L -> _connectionRequestService.getConnectionRequestsByTargetUid(to)
            else -> _connectionRequestService.getAllConnectionRequests()
        }
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connection-requests/{id}"], method = [RequestMethod.GET])
    fun getConnectionRequestById(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionRequestService.getConnectionRequestById(id)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connection-requests"], method = [RequestMethod.POST])
    fun createConnectionRequest(@RequestBody connectionRequest: BusinessCreateConnectionRequest, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionRequestService.createConnectionRequest(connectionRequest)
        return ControllerUtils.makeResponse(response, request)
    }

    @RequestMapping(value = ["connection-requests/{id}"], method = [RequestMethod.DELETE])
    fun deleteConnectionRequest(@PathVariable("id") id: Long, request: HttpServletRequest): ResponseEntity<*>? {
        val response: Response<*> = _connectionRequestService.deleteConnectionRequestById(id)
        return ControllerUtils.makeResponse(response, request)
    }
}
