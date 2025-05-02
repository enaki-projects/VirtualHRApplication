package org.enaki.license.manager_db.business.business_models.create

import org.enaki.license.manager_db.persistence.entities.Connection

class BusinessCreateConnection(
    var uid1: Long, var uid2: Long
) {
    fun getDuplicatedEntityMessage(): String {
        return "Connection with fields (uid1, uid2): (${uid1}, ${uid2}) already exists."
    }

    companion object{
        fun toConnection(connection: BusinessCreateConnection): Connection {
            return Connection(
                connectionId = -1,
                uid1 = connection.uid1,
                uid2 = connection.uid2,
            )
        }
    }
}