package org.enaki.license.manager_db.business.error_handling

object CustomResponse {
    fun <T> getItemNotFoundResponse(itemName: String, uid: Long): Response<T> {
        return Response(false, 404, null, String.format("%s with id %s not found", itemName, uid))
    }

    fun <T> getItemNotFoundResponse(itemName: String, value: String): Response<T> {
        return Response(false, 404, null, String.format("%s with value %s not found", itemName, value))
    }

    fun <T> getItemIsExistentResponse(message: String): Response<T> {
        return Response(false, 400, null, message)
    }
}