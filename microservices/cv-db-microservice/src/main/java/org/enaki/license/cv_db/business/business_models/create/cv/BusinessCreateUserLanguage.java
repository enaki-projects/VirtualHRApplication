package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;

public class BusinessCreateUserLanguage {
    public Long uid;
    public Long languageId;
    public String listening;
    public String reading;
    public String spokenInteraction;
    public String spokenProduction;
    public String writing;

    public BusinessCreateUserLanguage(Long uid, Long languageId, String listening, String reading, String spokenInteraction, String spokenProduction, String writing) {
        this.uid = uid;
        this.languageId = languageId;
        this.listening = listening;
        this.reading = reading;
        this.spokenInteraction = spokenInteraction;
        this.spokenProduction = spokenProduction;
        this.writing = writing;
    }

    public static UserLanguage toUserLanguage(BusinessCreateUserLanguage userLanguage){
        return new UserLanguage(
                Integer.toUnsignedLong(0),
                userLanguage.uid,
                userLanguage.languageId,
                userLanguage.listening,
                userLanguage.reading,
                userLanguage.spokenInteraction,
                userLanguage.spokenProduction,
                userLanguage.writing
        );
    }

    public boolean isValid() {
        return (uid != null &&
                languageId != null &&
                listening != null &&
                reading != null &&
                spokenInteraction != null &&
                spokenProduction != null &&
                writing != null);
    }

    public String getRequiredFieldsMessage(){
        return "User Language require fields (languageId, listening, reading, spokenInteraction, spokenProduction, writing)";
    }

    public String getDuplicatedEntityMessage() {
        return String.format("User Language with value fields (uid, languageId): (%s, %s) is existent", uid, languageId);
    }
}
