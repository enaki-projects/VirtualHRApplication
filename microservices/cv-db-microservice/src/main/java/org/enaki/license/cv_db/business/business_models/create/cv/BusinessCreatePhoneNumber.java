package org.enaki.license.cv_db.business.business_models.create.cv;

import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;

public class BusinessCreatePhoneNumber {
    public Long uid;
    public Long countryId;
    public String number;

    public BusinessCreatePhoneNumber(Long uid, Long countryId, String number) {
        this.uid = uid;
        this.countryId = countryId;
        this.number = number;
    }

    public static PhoneNumber toPhoneNumber(BusinessCreatePhoneNumber phoneNumber){
        return new PhoneNumber(
                Integer.toUnsignedLong(0),
                phoneNumber.uid,
                phoneNumber.countryId,
                phoneNumber.number
        );
    }

    public boolean isValid() {
        return (uid != null && countryId != null && number != null);
    }

    public String getRequiredFieldsMessage(){
        return "Phone Number require fields (countryId, number)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Phone Number with value fields (uid, countryId, number): (%s, %s, %s) is existent", uid, countryId, number);
    }
}
