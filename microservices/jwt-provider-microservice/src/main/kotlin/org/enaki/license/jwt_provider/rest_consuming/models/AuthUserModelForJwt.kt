package org.enaki.license.jwt_provider.rest_consuming.models

data class AuthUserModelForJwt
(
    var uid: Long? = null,
    var email: String? = null,
    var roles: List<RoleModel> = emptyList()
)
