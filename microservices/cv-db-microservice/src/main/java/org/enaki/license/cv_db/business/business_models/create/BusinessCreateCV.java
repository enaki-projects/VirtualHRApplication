package org.enaki.license.cv_db.business.business_models.create;

import org.enaki.license.cv_db.persistence.entities.CV;

import java.sql.Date;

public class BusinessCreateCV {
    public BusinessCreateCV(Long uid, String firstname, String lastname, String email, String gender, Date birthdate) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.birthdate = birthdate;
    }

    public Long uid;
    public String firstname;
    public String lastname;
    public String email;
    public String gender;
    public Date birthdate;

    public static CV toCV(BusinessCreateCV cv){
        return new CV(
                cv.uid,
                cv.firstname,
                cv.lastname,
                cv.email,
                cv.birthdate,
                cv.gender,
                null, null, null, null, true, false
        );
    }

    public boolean isValid() {
        return (uid != null) && (firstname != null) && (lastname != null) && (email != null) && (gender != null) && (birthdate != null);
    }

    public String getRequiredFieldsMessage(){
        return "CV require fields (uid, firstname, lastname, email, gender, birthdate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("CV with value fields (uid, firstname, lastname, email, gender, birthdate): (%s, %s, %s, %s, %s, %s) is existent",
                uid, firstname, lastname, email, gender, birthdate);
    }
}
