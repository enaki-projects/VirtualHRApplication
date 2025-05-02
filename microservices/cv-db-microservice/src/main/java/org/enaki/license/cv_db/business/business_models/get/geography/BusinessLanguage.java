package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Language;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessLanguage {
    public Long languageId;
    public String name;
    public String code;
    public String nativeName;

    public BusinessLanguage(Long languageId, String name, String code, String nativeName) {
        this.languageId = languageId;
        this.name = name;
        this.code = code;
        this.nativeName = nativeName;
    }

    public static BusinessLanguage toBusinessLanguage(Language language) {
        return new BusinessLanguage(language.languageId, language.name, language.code, language.nativeName);
    }

    public static List<BusinessLanguage> toBusinessLanguageList(List<Language> languageList) {
        return languageList.stream()
                .map(BusinessLanguage::toBusinessLanguage)
                .collect(Collectors.toList());
    }
}
