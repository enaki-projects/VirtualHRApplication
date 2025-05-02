package org.enaki.license.cv_db.persistence.entities.geography;

public class Language {
    public Long languageId;
    public String name;
    public String code;
    public String nativeName;

    public Language(Long languageId, String name, String code, String nativeName) {
        this.languageId = languageId;
        this.name = name;
        this.code = code;
        this.nativeName = nativeName;
    }
}
