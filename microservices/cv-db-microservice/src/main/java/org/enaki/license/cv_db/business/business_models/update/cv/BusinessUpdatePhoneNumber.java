package org.enaki.license.cv_db.business.business_models.update.cv;

import org.enaki.license.cv_db.business.business_models.create.cv.BusinessCreatePhoneNumber;
import org.enaki.license.cv_db.persistence.entities.cv.PhoneNumber;

public class BusinessUpdatePhoneNumber {
    public Long uid;
    public Long countryId;
    public String number;

    public BusinessUpdatePhoneNumber(Long uid, Long countryId, String number) {
        this.uid = uid;
        this.countryId = countryId;
        this.number = number;
    }

    public static PhoneNumber toPhoneNumber(Long phoneNumberId, BusinessUpdatePhoneNumber phoneNumber) {
        return new PhoneNumber(
                phoneNumberId,
                phoneNumber.uid,
                phoneNumber.countryId,
                phoneNumber.number
        );
    }

    public static BusinessCreatePhoneNumber toBusinessCreatePhoneNumber(BusinessUpdatePhoneNumber phoneNumber) {
        return new BusinessCreatePhoneNumber(
                phoneNumber.uid,
                phoneNumber.countryId,
                phoneNumber.number
        );
    }

    public boolean isValid() {
        return (uid != null && countryId != null && number != null);
    }

    public String getRequiredFieldsMessage(){
        return "Phone Number require fields (uid, countryId, number)";
    }

    public String getDuplicatedEntityMessage(){
        return String.format("Phone Number with value fields (uid, countryId, number): (%s, %s, %s) is existent", uid, countryId, number);
    }
}
