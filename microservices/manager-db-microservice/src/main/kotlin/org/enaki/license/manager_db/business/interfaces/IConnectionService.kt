package org.enaki.license.manager_db.business.interfaces

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateConnection
import org.enaki.license.manager_db.business.business_models.get.BusinessConnection
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.error_handling.Response

interface IConnectionService {

    fun getAllConnections(): Response<List<BusinessConnection>>
    fun getConnectionsByUserId(uid: Long): Response<*>
    fun getConnectionByUids(uid1: Long, uid2: Long): Response<BusinessConnection?>
    fun getConnectionById(id: Long): Response<BusinessConnection?>

    fun deleteConnectionById(id: Long): Response<*>
    fun createConnection(connection: BusinessCreateConnection): Response<BusinessId?>
}