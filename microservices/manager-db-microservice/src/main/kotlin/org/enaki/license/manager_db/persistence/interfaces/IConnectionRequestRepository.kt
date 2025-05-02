package org.enaki.license.manager_db.persistence.interfaces

import org.enaki.license.manager_db.persistence.entities.ConnectionRequest
import org.enaki.license.manager_db.persistence.interfaces.generic.IReadRepository

interface IConnectionRequestRepository: IReadRepository<ConnectionRequest> {
    fun add(item: ConnectionRequest): Long?
    fun delete(id: Long): Boolean
    fun getByDestinationUids(from: Long, to: Long): ConnectionRequest?
    fun getAllBySender(from: Long): List<ConnectionRequest>
    fun getAllByReceiver(to: Long): List<ConnectionRequest>
}