package org.enaki.license.manager_db.persistence.interfaces

import org.enaki.license.manager_db.persistence.entities.Connection
import org.enaki.license.manager_db.persistence.interfaces.generic.IReadRepository

interface IConnectionRepository: IReadRepository<Connection> {
    fun add(item: Connection): Long?
    fun delete(id: Long): Boolean
    fun getByUids(uid1: Long, uid2: Long): Connection?
    fun getAllByUid(uid: Long): List<Connection>
}