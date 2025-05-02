package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Award;

import java.sql.Date;

public class BusinessCreateAward {
    public Long uid;
    public String title;
    public String awardingInstitution;
    public Date dateOfAward;
    public String description;

    public BusinessCreateAward(Long uid, String title, String awardingInstitution, Date dateOfAward, String description) {
        this.uid = uid;
        this.title = title;
        this.awardingInstitution = awardingInstitution;
        this.dateOfAward = dateOfAward;
        this.description = description;
    }

    public static Award toAward(BusinessCreateAward award) {
        return new Award(
                Integer.toUnsignedLong(0),
                award.uid,
                award.title,
                award.awardingInstitution,
                award.dateOfAward,
                award.description
        );
    }

    public boolean isValid() {
        return (uid != null && title != null && awardingInstitution != null && dateOfAward != null);
    }

    public String getRequiredFieldsMessage(){
        return "Award require fields (title, awardingInstitution, dateOfAward)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Award with value fields (uid, title, awardingInstitution, dateOfAward): (%s, %s, %s, %s) is existent",
                uid, title, awardingInstitution, dateOfAward);
    }
}
