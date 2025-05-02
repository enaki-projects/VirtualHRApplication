package org.enaki.license.jwt_provider.rest_consuming.models

class AuthUserModelResponse
{
    var uid: Long? = null
    var email: String? = null
    var urlRoles: List<String>? = null
    override fun toString(): String {
        return String.format("Auth User: (%d, %s, %s)", uid, email, urlRoles)
    }
}
