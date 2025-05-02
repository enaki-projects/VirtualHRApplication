package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateHobby;
import org.enaki.license.cv_db.persistence.entities.cv.Hobby;

public class BusinessUpdateHobby {
    public Long uid;
    public String name;
    public String description;

    public BusinessUpdateHobby(Long uid, String name, String description) {
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    public static Hobby toHobby(Long hobbyId, BusinessUpdateHobby hobby) {
        return new Hobby(
                hobbyId,
                hobby.uid,
                hobby.name,
                hobby.description
        );
    }

    public static BusinessCreateHobby toBusinessCreateHobby(BusinessUpdateHobby hobby) {
        return new BusinessCreateHobby(
                hobby.uid,
                hobby.name,
                hobby.description
        );
    }

    public boolean isValid() {
        return (uid != null && name != null);
    }

    public String getRequiredFieldsMessage(){
        return "Hobby require fields (uid, name)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Hobby with value fields (uid, name): (%s, %s) is existent", uid, name);
    }
}
