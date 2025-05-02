package org.enaki.license.jwt_provider.rest_consuming.models

class RoleModel {
    var roleId: Long? = null
    var roleName: String? = null

    override fun toString(): String {
        return String.format("Role: (%d, %s)", roleId, roleName)
    }
}
