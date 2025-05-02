package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreateOrganisation;
import org.enaki.license.cv_db.persistence.entities.cv.Organisation;

public class BusinessUpdateOrganisation {
    public Long cityId;
    public String organisationName;
    public String address;

    public BusinessUpdateOrganisation(Long cityId, String organisationName, String address) {
        this.cityId = cityId;
        this.organisationName = organisationName;
        this.address = address;
    }

    public static Organisation toOrganization(Long organizationId, BusinessUpdateOrganisation organization) {
        return new Organisation(
                organizationId,
                organization.cityId,
                organization.organisationName,
                organization.address
        );
    }

    public static BusinessCreateOrganisation toBusinessCreateOrganization(BusinessUpdateOrganisation organization) {
        return new BusinessCreateOrganisation(
                organization.cityId,
                organization.organisationName,
                organization.address
        );
    }

    public boolean isValid() {
        return (cityId != null && organisationName != null);
    }

    public String getRequiredFieldsMessage(){
        return "Organisation require fields (cityId, organisationName)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Organisation with value fields (cityId, organisationName): (%s, %s) is existent", cityId, organisationName);
    }
}
