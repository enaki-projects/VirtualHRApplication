package org.enaki.license.manager_db.persistence.interfaces

import org.enaki.license.manager_db.persistence.entities.Cart
import org.enaki.license.manager_db.persistence.interfaces.generic.IRepository

interface ICartRepository: IRepository<Cart> {
    fun getByName(name: String): List<Cart>
    fun getByUid(uid: Long): List<Cart>
    fun getByUidWithCandidateBelonging(uid: Long, candidateId: Long): List<Long>
    fun getByUidWithMaintainerBelonging(uid: Long, maintainerId: Long): List<Long>
}