package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.Organisation;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessOrganisation {
    public Long organisationId;
    public Long cityId;
    public String organisationName;
    public String address;
    public HashMap<String, String> references = new HashMap<>();

    public BusinessOrganisation(Long organisationId, Long cityId, String organisationName, String address, String serverAddress) {
        this.organisationId = organisationId;
        this.cityId = cityId;
        this.organisationName = organisationName;
        this.address = address;
        this.references.put("cityReference", String.format("%s/api/geography/cities/%s", serverAddress, this.cityId));
    }

    public static BusinessOrganisation toBusinessOrganization(Organisation organisation, String serverAddress){
        return new BusinessOrganisation(
                organisation.organisationId,
                organisation.cityId,
                organisation.organisationName,
                organisation.address,
                serverAddress
        );
    }

    public static List<BusinessOrganisation> toBusinessOrganizationList(List<Organisation> organisationList, String serverAddress){
        return organisationList.stream()
                .map(it -> BusinessOrganisation.toBusinessOrganization(it, serverAddress))
                .collect(Collectors.toList());
    }
}
