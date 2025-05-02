package org.enaki.license.cv_db.business.error_handling;

public class Response<T> {
    public Boolean successfulOperation;
    public Integer code;
    public T data;
    public String error;
    public String message;

    public Response(Boolean successfulOperation, Integer code, T data, String error, String message) {
        this.successfulOperation = successfulOperation;
        this.code = code;
        this.data = data;
        this.error = error;
        this.message = message;
    }

    public Response(Boolean successfulOperation, Integer code, T data, String error) {
        this.successfulOperation = successfulOperation;
        this.code = code;
        this.data = data;
        this.error = error;
        this.message = "";
    }

    public Response(Boolean successfulOperation, Integer code, T data) {
        this.successfulOperation = successfulOperation;
        this.code = code;
        this.data = data;
        this.error = "";
        this.message = "";
    }
}
