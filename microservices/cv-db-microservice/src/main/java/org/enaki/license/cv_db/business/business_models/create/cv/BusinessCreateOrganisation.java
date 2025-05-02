package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Organisation;

public class BusinessCreateOrganisation {
    public Long cityId;
    public String organisationName;
    public String address;

    public BusinessCreateOrganisation(Long cityId, String organisationName, String address) {
        this.cityId = cityId;
        this.organisationName = organisationName;
        this.address = address;
    }

    public static Organisation toOrganization(BusinessCreateOrganisation organization){
        return new Organisation(
                Integer.toUnsignedLong(0),
                organization.cityId,
                organization.organisationName,
                organization.address
        );
    }

    public boolean isValid() {
        return (cityId != null && organisationName != null);
    }

    public String getRequiredFieldsMessage(){
        return "Organization require fields (cityId, organisationName)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Organisation with value fields (cityId, organisationName): (%s, %s) is existent", cityId, organisationName);
    }
}
