package org.enaki.license.jwt_provider.rest_consuming.connectors

import io.spring.guides.gs_producing_web_service.Credentials
import org.enaki.license.jwt_provider.rest_consuming.models.AuthUserModelRequest
import org.enaki.license.jwt_provider.rest_consuming.models.AuthUserModelResponse
import org.enaki.license.jwt_provider.rest_consuming.models.RoleModel
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Service
class AuthRestConnector(private val restTemplate: RestTemplate) {
    @Value("\${cvDbMicroserviceUrl}")
    private lateinit var cvDbUrl: String

    companion object {
        private val log: Logger = LoggerFactory.getLogger(AuthRestConnector::class.java)
    }

    private val entity: HttpEntity<*>

    fun getRoleModelByRoleUrl(roleUrl: String): RoleModel {
        // request user auth
        val builder = UriComponentsBuilder.fromHttpUrl(roleUrl)
        val response: HttpEntity<RoleModel> = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                RoleModel::class.java)
        return response.body!!
    }

    fun getAuthUserByCredentials(credentials: Credentials): AuthUserModelResponse {
        Assert.notNull(credentials.email, "The user's email must not be null")
        Assert.notNull(credentials.password, "The user's password must not be null")

        val requestHeaders = HttpHeaders()
        requestHeaders.contentType = MediaType.APPLICATION_JSON
        requestHeaders.accept = listOf(MediaType.APPLICATION_JSON)


        val authUserRequest = AuthUserModelRequest(credentials.email, credentials.password
        )
        val requestEntity = HttpEntity<AuthUserModelRequest>(authUserRequest, requestHeaders)

        // request user auth
        val response: HttpEntity<AuthUserModelResponse> = restTemplate.exchange(
                "$cvDbUrl/api/auth/login",
                HttpMethod.POST,
                requestEntity,
                AuthUserModelResponse::class.java)

        return response.body!!
    }


    init {
        // for rest consuming
        val headers = HttpHeaders()
        headers["Accept"] = MediaType.APPLICATION_JSON_VALUE
        entity = HttpEntity<Any>(headers)
    }
}