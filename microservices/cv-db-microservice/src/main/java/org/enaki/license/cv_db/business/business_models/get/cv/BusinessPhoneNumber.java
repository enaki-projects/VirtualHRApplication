package org.enaki.license.cv_db.business.business_models.get.cv;

import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BusinessPhoneNumber {
    public Long phoneNumberId;
    public Long uid;
    public Long countryId;
    public String number;
    public HashMap<String, String> references = new HashMap<>();

    public BusinessPhoneNumber(Long phoneNumberId, Long uid, Long countryId, String number, String serverAddress){
        this.phoneNumberId = phoneNumberId;
        this.uid = uid;
        this.countryId = countryId;
        this.number = number;
        this.references.put("countryReference", String.format("%s/api/geography/countries/%s", serverAddress, this.countryId));
    }

    public static BusinessPhoneNumber toBusinessPhoneNumber(PhoneNumber phoneNumber, String serverAddress){
        return new BusinessPhoneNumber(
                phoneNumber.phoneNumberId,
                phoneNumber.uid,
                phoneNumber.countryId,
                phoneNumber.number,
                serverAddress
        );
    }

    public static List<BusinessPhoneNumber> toBusinessPhoneNumberList(List<PhoneNumber> phoneNumberList, String serverAddress){
        return phoneNumberList.stream()
                .map(it -> BusinessPhoneNumber.toBusinessPhoneNumber(it, serverAddress))
                .collect(Collectors.toList());
    }
}
