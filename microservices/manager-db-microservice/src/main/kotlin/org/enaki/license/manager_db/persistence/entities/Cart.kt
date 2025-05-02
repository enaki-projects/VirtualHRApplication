package org.enaki.license.manager_db.persistence.entities

data class Cart(
    var cartId: Long,
    var name: String,
    var creatorId: Long,
    var description: String?
);
