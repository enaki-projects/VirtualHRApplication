package org.enaki.license.jwt_provider.endpoints


import io.spring.guides.gs_producing_web_service.*
import org.enaki.license.jwt_provider.exception_handling.ServiceFault
import org.enaki.license.jwt_provider.exception_handling.ServiceFaultException
import org.enaki.license.jwt_provider.rest_consuming.adapters.AuthRestAdapter
import org.enaki.license.jwt_provider.security.JwtTokenProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.client.HttpClientErrorException
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import java.net.ConnectException


@Endpoint
class AuthEndpoint @Autowired constructor(private val authRestAdapter: AuthRestAdapter, private val jwtTokenProvider: JwtTokenProvider) {
    companion object {
        const val NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service"
        private val log = LoggerFactory.getLogger(AuthEndpoint::class.java)
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAuthRequest")
    @ResponsePayload
    @Throws(ServiceFaultException::class)
    fun getUserInfo(@RequestPayload request: GetAuthRequest): GetAuthResponse {
        val response = GetAuthResponse()
        return try {
            val user: User = authRestAdapter.authenticate(request.credentials)
            response.user = user
            response
        } catch (e: HttpClientErrorException) {
            throw ServiceFaultException("ERROR", ServiceFault(
                    "401 UNAUTHORISED", "Credentials are incorrect."))
        } catch (e: ConnectException) {
            throw ServiceFaultException("ERROR", ServiceFault(
                    "503 SERVICE_UNAVAILABLE", "Connection with DatabaseService cannot be established."))
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEncodedAuthRequest")
    @ResponsePayload
    @Throws(ServiceFaultException::class)
    fun getUserInfoEncoded(@RequestPayload request: GetEncodedAuthRequest): GetAuthResponse {
        val response = GetAuthResponse()
        return try {
            val user: User = authRestAdapter.authenticate(request.credentials, encodeBase64 = true)
            response.user = user
            response
        } catch (e: HttpClientErrorException) {
            throw ServiceFaultException("ERROR", ServiceFault(
                    "404 NOT_FOUND", "Credentials are incorrect."))
        } catch (e: ConnectException) {
            throw ServiceFaultException("ERROR", ServiceFault(
                    "503 SERVICE_UNAVAILABLE", "Connection with DatabaseService cannot be established."))
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getDecodedJwtRequest")
    @ResponsePayload
    @Throws(ServiceFaultException::class)
    fun getDecodedJwt(@RequestPayload request: GetDecodedJwtRequest): GetDecodedJwtResponse {
        try {
            val response = GetDecodedJwtResponse()

            val decodedJwt = jwtTokenProvider.decodeWithBase64(request.encodedJwt)
            val decodedJwtObject = jwtTokenProvider.parseJwt(decodedJwt)
            response.decodedJwt = decodedJwt
            return response
        }catch (e: IllegalArgumentException) {
            throw ServiceFaultException("ERROR", ServiceFault(
                    "400 BAD_REQUEST", e.message))
        }
    }
}
