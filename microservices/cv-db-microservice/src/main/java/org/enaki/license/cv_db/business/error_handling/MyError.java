package org.enaki.license.cv_db.business.error_handling;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyError {
    public String timestamp;
    public Integer status;
    public String error;
    public String message;
    public String path;

    public MyError(Integer status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
        this.path = path;
    }
}
