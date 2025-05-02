package org.enaki.license.cv_db.persistence.entities.cv;

public class PhoneNumber {
    public Long phoneNumberId;
    public Long uid;
    public Long countryId;
    public String number;

    public PhoneNumber(Long phoneNumberId, Long uid, Long countryId, String number) {
        this.phoneNumberId = phoneNumberId;
        this.uid = uid;
        this.countryId = countryId;
        this.number = number;
    }
}
