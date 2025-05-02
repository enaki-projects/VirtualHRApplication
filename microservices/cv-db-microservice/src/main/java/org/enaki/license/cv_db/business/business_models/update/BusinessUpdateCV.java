package org.enaki.license.cv_db.business.business_models.update;

import org.enaki.license.cv_db.persistence.entities.CV;

import java.sql.Date;

public class BusinessUpdateCV {
    public Long uid;
    public String firstname;
    public String lastname;
    public String email;
    public String gender;
    public Date birthdate;
    public String aboutMe;
    public String motto;
    public Integer iqScore;

    public BusinessUpdateCV(Long uid, String firstname, String lastname, String email, String gender, Date birthdate,  String aboutMe, String motto, Integer iqScore) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.aboutMe = aboutMe;
        this.motto = motto;
        this.iqScore = iqScore;
    }

    public static CV toCV(Long uid, BusinessUpdateCV cv){
        return new CV(
                uid,
                cv.firstname,
                cv.lastname,
                cv.email,
                cv.birthdate,
                cv.gender,
                cv.aboutMe, cv.motto, cv.iqScore, null,
                true, false
        );
    }

    public boolean isValid() {
        return (firstname != null) && (lastname != null) && (email != null) && (gender != null) && (birthdate != null);
    }

    public String getRequiredFieldsMessage(){
        return "Update CV require fields (firstname, lastname, email, gender, birthdate)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Update CV with value fields (uid, firstname, lastname, email, gender, birthdate): (%s, %s, %s, %s, %s, %s) is existent",
                uid, firstname, lastname, email, gender, birthdate);
    }
}
