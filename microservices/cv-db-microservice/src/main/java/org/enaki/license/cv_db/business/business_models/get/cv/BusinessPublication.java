package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Publication;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessPublication {
    public Long publicationId;
    public Long uid;
    public String title;
    public String description;
    public Date year;
    public String url;
    public String reference;

    public BusinessPublication(Long publicationId, Long uid, String title, String description, Date year, String url, String reference) {
        this.publicationId = publicationId;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.year = year;
        this.url = url;
        this.reference = reference;
    }

    public static BusinessPublication toBusinessPublication(Publication publication){
        return new BusinessPublication(
                publication.publicationId,
                publication.uid,
                publication.title,
                publication.description,
                publication.year,
                publication.url,
                publication.reference
        );
    }

    public static List<BusinessPublication> toBusinessPublicationList(List<Publication> publicationList){
        return publicationList.stream()
                .map(BusinessPublication::toBusinessPublication)
                .collect(Collectors.toList());
    }
}
