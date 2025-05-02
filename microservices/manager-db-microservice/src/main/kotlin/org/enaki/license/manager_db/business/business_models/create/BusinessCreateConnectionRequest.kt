package org.enaki.license.manager_db.business.business_models.create

import org.enaki.license.manager_db.persistence.entities.ConnectionRequest

class BusinessCreateConnectionRequest(
    var from: Long, var to: Long
) {
    fun getDuplicatedEntityMessage(): String {
        return "Request Connection with fields (from, to): (${from}, ${to}) already exists."
    }

    companion object{
        fun toConnectionRequest(connectionRequest: BusinessCreateConnectionRequest): ConnectionRequest {
            return ConnectionRequest(
                connectionRequestId = -1,
                from = connectionRequest.from,
                to = connectionRequest.to,
            )
        }
    }
}