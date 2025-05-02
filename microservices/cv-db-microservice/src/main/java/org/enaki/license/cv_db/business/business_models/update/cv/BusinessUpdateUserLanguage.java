package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateUserLanguage;
import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;

public class BusinessUpdateUserLanguage {
    public Long uid;
    public Long languageId;
    public String listening;
    public String reading;
    public String spokenInteraction;
    public String spokenProduction;
    public String writing;

    public BusinessUpdateUserLanguage(Long uid, Long languageId, String listening, String reading, String spokenInteraction, String spokenProduction, String writing) {
        this.uid = uid;
        this.languageId = languageId;
        this.listening = listening;
        this.reading = reading;
        this.spokenInteraction = spokenInteraction;
        this.spokenProduction = spokenProduction;
        this.writing = writing;
    }

    public static UserLanguage toUserLanguage(Long userLanguageId, BusinessUpdateUserLanguage userLanguage) {
        return new UserLanguage(
                userLanguageId,
                userLanguage.uid,
                userLanguage.languageId,
                userLanguage.listening,
                userLanguage.reading,
                userLanguage.spokenInteraction,
                userLanguage.spokenProduction,
                userLanguage.writing
        );
    }

    public static BusinessCreateUserLanguage toBusinessCreateUserLanguage(BusinessUpdateUserLanguage userLanguage) {
        return new BusinessCreateUserLanguage(
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
        return "User Language require fields (uid, languageId, listening, reading, spokenInteraction, spokenProduction, writing)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("User Language with value fields (uid, languageId): (%s, %s) is existent", uid, languageId);
    }
}
