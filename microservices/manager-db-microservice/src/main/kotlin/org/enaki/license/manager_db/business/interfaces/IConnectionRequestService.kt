package org.enaki.license.manager_db.business.interfaces

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateConnectionRequest
import org.enaki.license.manager_db.business.business_models.get.BusinessConnectionRequest
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.error_handling.Response

interface IConnectionRequestService {
    fun getAllConnectionRequests(): Response<List<BusinessConnectionRequest>>
    fun getConnectionRequestsBySenderUid(from: Long): Response<*>
    fun getConnectionRequestsByTargetUid(to: Long): Response<*>
    fun getConnectionRequestByDestinationUids(from: Long, to: Long): Response<BusinessConnectionRequest?>
    fun getConnectionRequestById(id: Long): Response<BusinessConnectionRequest?>

    fun deleteConnectionRequestById(id: Long): Response<*>
    fun createConnectionRequest(connectionRequest: BusinessCreateConnectionRequest): Response<BusinessId?>
}