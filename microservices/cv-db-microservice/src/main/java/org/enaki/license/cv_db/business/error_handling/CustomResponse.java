package org.enaki.license.cv_db.business.error_handling;

public class CustomResponse {
    public static <T> Response<T> getItemNotFoundResponse(String itemName, Long uid){
        return new Response<>(false, 404, null, String.format("%s with id %s not found", itemName, uid));
    }

    public static <T> Response<T> getItemNotFoundResponse(String itemName, String value){
        return new Response<>(false, 404, null, String.format("%s with value %s not found", itemName, value));
    }

    public static <T> Response<T> getItemIsExistentResponse(String message){
        return new Response<>(false, 400, null, message);
    }
}
