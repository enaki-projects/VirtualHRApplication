package org.enaki.license.cv_db.persistence.entities;

import java.sql.Date;

public class CV {
    public Long uid;
    public String firstname;
    public String lastname;
    public String email;
    public Date birthdate;
    public String gender;
    public String aboutMe;
    public String motto;
    public Integer iqScore;
    public String imageName;
    public Boolean canTakeTheIqRequest;
    public Boolean requestedRetakeOfIqTest;

    public CV(Long uid, String firstname, String lastname, String email, Date birthdate, String gender, String aboutMe, String motto, Integer iqScore, String imageName, Boolean canTakeTheIqRequest, Boolean requestedRetakeOfIqTest) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.birthdate = birthdate;
        this.gender = gender;
        this.aboutMe = aboutMe;
        this.motto = motto;
        this.iqScore = iqScore;
        this.imageName = imageName;
        this.canTakeTheIqRequest = canTakeTheIqRequest;
        this.requestedRetakeOfIqTest = requestedRetakeOfIqTest;
    }
}


