package org.enaki.license.jwt_provider.security
import java.util.*

class DecodedJwt {
    var uid: String? = null
    var jti: String? = null
    var expiration: Date? = null
    var roles: List<String>? = null
    val isExpired: Boolean
        get() {
            val today = Date()
            return today.after(expiration)
        }
}
