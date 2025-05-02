package org.enaki.license.manager_db.business.business_models.get

import org.enaki.license.manager_db.persistence.entities.ConnectionRequest

class BusinessConnectionRequest(
    var connectionRequestId: Long, var from: Long, var to: Long
) {
    companion object{
        fun toBusinessConnectionRequest(connection: ConnectionRequest): BusinessConnectionRequest {
            return BusinessConnectionRequest(
                connectionRequestId = connection.connectionRequestId,
                from = connection.from,
                to = connection.to,
            )
        }

        fun toBusinessConnectionRequestList(requestConnections: List<ConnectionRequest>): List<BusinessConnectionRequest>{
            return requestConnections.map { BusinessConnectionRequest.toBusinessConnectionRequest(it) }
        }

        fun toBusinessConnectionRequestFrom(
            from: Long,
            requestConnections: List<ConnectionRequest>
        ): HashMap<String, Any> {
            val destinations = requestConnections
                .filter { it.from == from }
                .map { it -> it.to }
            return hashMapOf(
                "from" to from,
                "to" to destinations
            )
        }

        fun toBusinessConnectionRequestTo(to: Long, requestConnections: List<ConnectionRequest>): HashMap<String, Any> {
            val senders = requestConnections
                .filter { it.to == to }
                .map { it -> it.from }
            return hashMapOf(
                "to" to to,
                "from" to senders
            )
        }
    }
}