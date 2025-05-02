package org.enaki.license.manager_db.persistence.entities

data class ConnectionRequest(
    var connectionRequestId: Long,
    var from: Long,
    var to: Long
)
