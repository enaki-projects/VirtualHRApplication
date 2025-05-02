package org.enaki.license.manager_db.persistence.entities

data class CartItem(
    var cartItemId: Long,
    var cartId: Long,
    var candidateId: Long,
    var score: Float?,
    var notes: String?
);