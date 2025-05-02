package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Hobby;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessHobby {
    public Long hobbyId;
    public Long uid;
    public String name;
    public String description;

    public BusinessHobby(Long hobbyId, Long uid, String name, String description) {
        this.hobbyId = hobbyId;
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    public static BusinessHobby toBusinessHobby(Hobby hobby){
        return new BusinessHobby(
                hobby.hobbyId,
                hobby.uid,
                hobby.name,
                hobby.description
        );
    }

    public static List<BusinessHobby> toBusinessHobbyList(List<Hobby> hobbyList){
        return hobbyList.stream()
                .map(BusinessHobby::toBusinessHobby)
                .collect(Collectors.toList());
    }
}
