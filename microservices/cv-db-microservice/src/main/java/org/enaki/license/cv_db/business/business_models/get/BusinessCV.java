package org.enaki.license.cv_db.business.business_models.get;

import org.enaki.license.cv_db.persistence.entities.CV;

import java.sql.Date;

public class BusinessCV {
    public BusinessCV(Long uid, String firstname, String lastname, String email, String gender, Date birthdate, String aboutMe, String motto, Integer iqScore, Boolean hasImage, Boolean canTakeTheIqRequest, Boolean requestedRetakeOfIqTest) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.birthdate = birthdate;
        this.aboutMe = aboutMe;
        this.motto = motto;
        this.iqScore = iqScore;
        this.hasImage = hasImage;
        this.canTakeTheIqRequest = canTakeTheIqRequest;
        this.requestedRetakeOfIqTest = requestedRetakeOfIqTest;
    }

    public Long uid;
    public String firstname;
    public String lastname;
    public String email;
    public String gender;
    public Date birthdate;
    public String aboutMe;
    public String motto;
    public Integer iqScore;
    public Boolean hasImage;
    public Boolean canTakeTheIqRequest;
    public Boolean requestedRetakeOfIqTest;
    public BusinessCVReferences cvReferences;

    public static BusinessCV toBusinessCV(CV cv){
        return new BusinessCV(
                cv.uid,
                cv.firstname,
                cv.lastname,
                cv.email,
                cv.gender,
                cv.birthdate,
                cv.aboutMe,
                cv.motto,
                cv.iqScore,
                cv.imageName != null,
                cv.canTakeTheIqRequest,
                cv.requestedRetakeOfIqTest
        );
    }
}
