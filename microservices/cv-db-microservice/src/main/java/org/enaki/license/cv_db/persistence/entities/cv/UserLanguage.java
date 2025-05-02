package org.enaki.license.cv_db.persistence.entities.cv;

public class UserLanguage {
    public Long userLanguageId;
    public Long uid;
    public Long languageId;
    public String listening;
    public String reading;
    public String spokenInteraction;
    public String spokenProduction;
    public String writing;

    public UserLanguage(Long userLanguageId, Long uid, Long languageId, String listening, String reading, String spokenInteraction, String spokenProduction, String writing) {
        this.userLanguageId = userLanguageId;
        this.uid = uid;
        this.languageId = languageId;
        this.listening = listening;
        this.reading = reading;
        this.spokenInteraction = spokenInteraction;
        this.spokenProduction = spokenProduction;
        this.writing = writing;
    }
}
