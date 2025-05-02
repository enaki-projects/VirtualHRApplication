package org.enaki.license.cv_db.presentation.utils;

import org.enaki.license.cv_db.business.error_handling.MyError;
import org.enaki.license.cv_db.business.error_handling.Response;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtils {
    public static ResponseEntity<?> makeResponse(Response<?> response, HttpServletRequest request){
        if (response.successfulOperation)
            return ResponseEntity.status(response.code).body(response.data);
        else{
            MyError err = new MyError(response.code, response.error, response.message, request.getRequestURI());
            return ResponseEntity.status(response.code).body(err);
        }
    }
}


