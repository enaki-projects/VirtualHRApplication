package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Hobby;

public class BusinessCreateHobby {
    public Long uid;
    public String name;
    public String description;

    public BusinessCreateHobby(Long uid, String name, String description) {
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    public static Hobby toHobby(BusinessCreateHobby hobby){
        return new Hobby(
                Integer.toUnsignedLong(0),
                hobby.uid,
                hobby.name,
                hobby.description
        );
    }

    public boolean isValid() {
        return (uid != null && name != null);
    }

    public String getRequiredFieldsMessage(){
        return "Hobby require fields (name)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Hobby with value fields (uid, name): (%s, %s) is existent", uid, name);
    }
}
