package org.enaki.license.cv_db.persistence.sqlite.utils;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateParser {
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parse (String date){
        try {
            return new Date(simpleDateFormat.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
