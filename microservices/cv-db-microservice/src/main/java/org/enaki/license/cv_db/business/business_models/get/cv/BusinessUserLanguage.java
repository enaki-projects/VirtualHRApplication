package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.UserLanguage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessUserLanguage {
    public Long userLanguageId;
    public Long uid;
    public Long languageId;
    public String listening;
    public String reading;
    public String spokenInteraction;
    public String spokenProduction;
    public String writing;
    public HashMap<String, String> references = new HashMap<>();

    public BusinessUserLanguage(Long userLanguageId, Long uid, Long languageId, String listening, String reading, String spokenInteraction, String spokenProduction, String writing, String serverAddress) {
        this.userLanguageId = userLanguageId;
        this.uid = uid;
        this.languageId = languageId;
        this.listening = listening;
        this.reading = reading;
        this.spokenInteraction = spokenInteraction;
        this.spokenProduction = spokenProduction;
        this.writing = writing;
        this.references.put("languageReference", String.format("%s/api/geography/languages/%s", serverAddress, this.languageId));
    }

    public static BusinessUserLanguage toBusinessUserLanguage(UserLanguage userLanguage, String serverAddress){
        return new BusinessUserLanguage(
                userLanguage.userLanguageId,
                userLanguage.uid,
                userLanguage.languageId,
                userLanguage.listening,
                userLanguage.reading,
                userLanguage.spokenInteraction,
                userLanguage.spokenProduction,
                userLanguage.writing,
                serverAddress
        );
    }

    public static List<BusinessUserLanguage> toBusinessUserLanguageList(List<UserLanguage> userLanguageList, String serverAddress){
        return userLanguageList.stream()
                .map(it -> BusinessUserLanguage.toBusinessUserLanguage(it, serverAddress))
                .collect(Collectors.toList());
    }
}
