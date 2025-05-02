package org.enaki.license.jwt_provider.rest_consuming.adapters

import io.spring.guides.gs_producing_web_service.Credentials
import io.spring.guides.gs_producing_web_service.User
import org.enaki.license.jwt_provider.rest_consuming.connectors.AuthRestConnector
import org.enaki.license.jwt_provider.rest_consuming.models.AuthUserModelForJwt
import org.enaki.license.jwt_provider.rest_consuming.models.AuthUserModelResponse
import org.enaki.license.jwt_provider.security.JwtTokenProvider
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.ConnectException

@Component
class AuthRestAdapter @Autowired constructor(private val authRestConnector: AuthRestConnector,
                                             private val jwtTokenProvider: JwtTokenProvider) {
    @Throws(ConnectException::class)
    fun authenticate(credentials: Credentials, encodeBase64: Boolean= false): User {
        log.debug(credentials.toString())
        val authUserResponse: AuthUserModelResponse = authRestConnector.getAuthUserByCredentials(credentials)
        val roles = authUserResponse.urlRoles!!.map { it -> authRestConnector.getRoleModelByRoleUrl(it) }

        val authUserForJwt = AuthUserModelForJwt(uid=authUserResponse.uid, email = authUserResponse.email, roles=roles)
        val jwt = jwtTokenProvider.createToken(authUserForJwt)

        val user = User()
        user.uid = authUserResponse.uid.toString()
        user.email = authUserResponse.email
        user.token = if (encodeBase64) jwtTokenProvider.encodeWithBase64(jwt) else jwt
        return user
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(AuthRestAdapter::class.java)
    }
}