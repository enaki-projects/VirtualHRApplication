package org.enaki.license.manager_db.presentation.utils

import org.enaki.license.manager_db.business.error_handling.MyError
import org.enaki.license.manager_db.business.error_handling.Response
import org.springframework.http.ResponseEntity
import jakarta.servlet.http.HttpServletRequest

object ControllerUtils {
    fun makeResponse(response: Response<*>, request: HttpServletRequest): ResponseEntity<*> {
        return if (response.successfulOoperation) ResponseEntity.status(response.code).body<Any>(response.data) else {
            val err = MyError(response.code, response.error, response.message, request.requestURI)
            ResponseEntity.status(response.code).body<Any>(err)
        }
    }
}