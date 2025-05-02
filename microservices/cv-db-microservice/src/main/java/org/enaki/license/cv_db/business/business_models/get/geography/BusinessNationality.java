package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Nationality;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessNationality {
    public Long nationalityId;
    public String nationalityName;

    public BusinessNationality(Long nationalityId, String nationalityName) {
        this.nationalityId = nationalityId;
        this.nationalityName = nationalityName;
    }

    public static BusinessNationality toBusinessNationality(Nationality nationality) {
        return new BusinessNationality(nationality.nationalityId, nationality.nationalityName);
    }

    public static List<BusinessNationality> toBusinessNationalityList(List<Nationality> nationalityList) {
        return nationalityList.stream()
                .map(BusinessNationality::toBusinessNationality)
                .collect(Collectors.toList());
    }
}
