package org.enaki.license.manager_db.business.services

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateConnection
import org.enaki.license.manager_db.business.business_models.get.BusinessConnection
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.error_handling.CustomResponse
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.IConnectionService
import org.enaki.license.manager_db.persistence.entities.Connection
import org.enaki.license.manager_db.persistence.interfaces.IConnectionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.stereotype.Service

@Service
class ConnectionService: IConnectionService {
    @Autowired
    private lateinit var _connectionRepository: IConnectionRepository
    
    override fun getAllConnections(): Response<List<BusinessConnection>> {
        return try {
            val connections: List<Connection> = _connectionRepository.getAll()
            Response(true, 200, BusinessConnection.toBusinessConnectionList(connections))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionsByUserId(uid: Long): Response<*> {
        return try {
            val connections: List<Connection> = _connectionRepository.getAllByUid(uid)
            Response(true, 200, BusinessConnection.toBusinessConnectionsOfUid(uid, connections))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionByUids(uid1: Long, uid2: Long): Response<BusinessConnection?> {
        return try {
            val connection = _connectionRepository.getByUids(uid1,uid2) ?: return CustomResponse.getItemNotFoundResponse("Connection", "(uid1, uid2): (${uid1}, ${uid2})")
            Response(true, 200, BusinessConnection.toBusinessConnection(connection))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionById(id: Long): Response<BusinessConnection?> {
        return try {
            val connection = _connectionRepository.get(id) ?: return CustomResponse.getItemNotFoundResponse("Connection", id)
            Response(true, 200, BusinessConnection.toBusinessConnection(connection))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteConnectionById(id: Long): Response<*> {
        return try {
            val success: Boolean = _connectionRepository.delete(id)
            if (!success){
                return CustomResponse.getItemNotFoundResponse<Unit>("Connection", id)
            }
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun createConnection(connection: BusinessCreateConnection): Response<BusinessId?> {
        return try {
            if (connection.uid1 == connection.uid2){
                return CustomResponse.getItemIsExistentResponse("Connection can't be established with the same user.")
            }

            val dbConnection = _connectionRepository.getByUids(connection.uid1, connection.uid2)
            if (dbConnection != null){
                return CustomResponse.getItemIsExistentResponse(connection.getDuplicatedEntityMessage())
            }

            val id: Long = _connectionRepository.add(BusinessCreateConnection.toConnection(connection))
                ?: return Response(false, 400, null, "Can't create connection")
            Response(true, 201, BusinessId(id))
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse(connection.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }
}