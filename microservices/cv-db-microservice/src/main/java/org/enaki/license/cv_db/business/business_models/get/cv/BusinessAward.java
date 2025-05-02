package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Award;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessAward {
    public Long awardId;
    public Long uid;
    public String title;
    public String awardingInstitution;
    public Date dateOfAward;
    public String description;

    public BusinessAward(Long awardId, Long uid, String title, String awardingInstitution, Date dateOfAward, String description) {
        this.awardId = awardId;
        this.uid = uid;
        this.title = title;
        this.awardingInstitution = awardingInstitution;
        this.dateOfAward = dateOfAward;
        this.description = description;
    }

    public static BusinessAward toBusinessAward(Award award){
        return new BusinessAward(
                award.awardId, award.uid, award.title, award.awardingInstitution, award.dateOfAward, award.description
        );
    }

    public static List<BusinessAward> toBusinessAwardList(List<Award> awardList){
        return awardList.stream()
                .map(BusinessAward::toBusinessAward)
                .collect(Collectors.toList());
    }
}
