package org.enaki.license.manager_db.persistence.interfaces

import org.enaki.license.manager_db.persistence.entities.CartMaintainer
import org.enaki.license.manager_db.persistence.interfaces.generic.IRepository

interface ICartMaintainerRepository: IRepository<CartMaintainer> {
    fun getAllByCartId(cartId: Long): List<CartMaintainer>
    fun getCartsByUidWithSpecificRole(maintainerId: Long, role: String = "editor"): List<Long>

    fun deleteAllByCartId(cartId: Long): Boolean
    fun deleteMaintainerFromCartId(cartId: Long, maintainerId: Long): Boolean
}