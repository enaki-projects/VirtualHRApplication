package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePublication;
import org.enaki.license.cv_db.persistence.entities.cv.Publication;

import java.sql.Date;

public class BusinessUpdatePublication {
    public Long uid;
    public String title;
    public String description;
    public Date year;
    public String url;
    public String reference;

    public BusinessUpdatePublication(Long uid, String title, String description, Date year, String url, String reference) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.year = year;
        this.url = url;
        this.reference = reference;
    }

    public static Publication toPublication(Long publicationId, BusinessUpdatePublication publication) {
        return new Publication(
                publicationId,
                publication.uid,
                publication.title,
                publication.description,
                publication.year,
                publication.url,
                publication.reference
        );
    }

    public static BusinessCreatePublication toBusinessCreatePublication(BusinessUpdatePublication publication) {
        return new BusinessCreatePublication(
                publication.uid,
                publication.title,
                publication.description,
                publication.year,
                publication.url,
                publication.reference
        );
    }

    public boolean isValid() {
        return (uid != null && title != null && year != null);
    }

    public String getRequiredFieldsMessage(){
        return "Publication require fields (uid, title, year)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Publication with value fields (uid, title, year): (%s, %s, %s) is existent", uid, title, year);
    }
}

