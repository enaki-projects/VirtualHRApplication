package org.enaki.license.manager_db.business.business_models.get

import org.enaki.license.manager_db.persistence.entities.Connection

class BusinessConnection(
    var connectionId: Long, var uid1: Long, var uid2: Long
) {
    companion object{
        fun toBusinessConnection(connection: Connection): BusinessConnection {
            return BusinessConnection(
                connectionId = connection.connectionId,
                uid2 = connection.uid2,
                uid1 = connection.uid1,
            )
        }

        fun toBusinessConnectionList(carts: List<Connection>): List<BusinessConnection>{
            return carts.map { BusinessConnection.toBusinessConnection(it) }
        }

        fun toBusinessConnectionsOfUid(uid: Long, requestConnections: List<Connection>): HashMap<String, Any> {
            val connections = requestConnections
                .filter { it.uid1 == uid || it.uid2 == uid }
                .map { if (it.uid1 == uid) it.uid2 else it.uid1 }
            return hashMapOf(
                "uid" to uid,
                "connections" to connections
            )
        }
    }
}