package org.enaki.license.manager_db.business.services

import org.enaki.license.manager_db.business.business_models.create.BusinessCreateConnectionRequest
import org.enaki.license.manager_db.business.business_models.get.BusinessConnectionRequest
import org.enaki.license.manager_db.business.business_models.get.BusinessId
import org.enaki.license.manager_db.business.error_handling.CustomResponse
import org.enaki.license.manager_db.business.error_handling.Response
import org.enaki.license.manager_db.business.interfaces.IConnectionRequestService
import org.enaki.license.manager_db.persistence.entities.ConnectionRequest
import org.enaki.license.manager_db.persistence.interfaces.IConnectionRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.UncategorizedSQLException
import org.springframework.stereotype.Service

@Service
class ConnectionRequestService: IConnectionRequestService {
    @Autowired
    private lateinit var _connectionRequestRepository: IConnectionRequestRepository
    
    override fun getAllConnectionRequests(): Response<List<BusinessConnectionRequest>> {
        return try {
            val requestConnectionRequests: List<ConnectionRequest> = _connectionRequestRepository.getAll()
            Response(true, 200, BusinessConnectionRequest.toBusinessConnectionRequestList(requestConnectionRequests))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionRequestsBySenderUid(from: Long): Response<*> {
        return try {
            val requestConnectionRequests: List<ConnectionRequest> = _connectionRequestRepository.getAllBySender(from)
            Response(true, 200, BusinessConnectionRequest.toBusinessConnectionRequestFrom(from, requestConnectionRequests))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionRequestsByTargetUid(to: Long): Response<*> {
        return try {
            val requestConnectionRequests: List<ConnectionRequest> = _connectionRequestRepository.getAllByReceiver(to)
            Response(true, 200, BusinessConnectionRequest.toBusinessConnectionRequestTo(to, requestConnectionRequests))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionRequestByDestinationUids(from: Long, to: Long): Response<BusinessConnectionRequest?> {
        return try {
            val requestConnectionRequest = _connectionRequestRepository.getByDestinationUids(from,to) ?: return CustomResponse.getItemNotFoundResponse("Connection Request", "(from, to): (${from}, ${to})")
            Response(true, 200, BusinessConnectionRequest.toBusinessConnectionRequest(requestConnectionRequest))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun getConnectionRequestById(id: Long): Response<BusinessConnectionRequest?> {
        return try {
            val requestConnectionRequest = _connectionRequestRepository.get(id) ?: return CustomResponse.getItemNotFoundResponse("ConnectionRequest", id)
            Response(true, 200, BusinessConnectionRequest.toBusinessConnectionRequest(requestConnectionRequest))
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun deleteConnectionRequestById(id: Long): Response<*> {
        return try {
            val success: Boolean = _connectionRequestRepository.delete(id)
            if (!success){
                return CustomResponse.getItemNotFoundResponse<Unit>("ConnectionRequest", id)
            }
            Response(true, 204, null)
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }

    override fun createConnectionRequest(connectionRequest: BusinessCreateConnectionRequest): Response<BusinessId?> {
        return try {
            if (connectionRequest.from == connectionRequest.to){
                return CustomResponse.getItemIsExistentResponse("Connection Request can't be established with the same user.")
            }

            val dbConnectionRequest = _connectionRequestRepository.getByDestinationUids(connectionRequest.from, connectionRequest.to)
            if (dbConnectionRequest != null){
                return CustomResponse.getItemIsExistentResponse(connectionRequest.getDuplicatedEntityMessage())
            }

            val dbConnectionRequest2 = _connectionRequestRepository.getByDestinationUids(connectionRequest.to, connectionRequest.from)
            if (dbConnectionRequest2 != null){
                return CustomResponse.getItemIsExistentResponse("User ${connectionRequest.to} has already sent a connection requet to user ${connectionRequest.from}")
            }

            val id: Long = _connectionRequestRepository.add(BusinessCreateConnectionRequest.toConnectionRequest(connectionRequest))
                ?: return Response(false, 400, null, "Can't create Connection Request")
            Response(true, 201, BusinessId(id))
        } catch (e: UncategorizedSQLException) {
            CustomResponse.getItemIsExistentResponse(connectionRequest.getDuplicatedEntityMessage())
        } catch (e: Exception) {
            Response(false, 400, null, e.toString())
        }
    }
}