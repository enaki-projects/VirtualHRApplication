package org.enaki.license.cv_db.persistence.entities.geography;

public class Country {
    public Long countryId;
    public String countryName;
    public String iso3;
    public String iso2;
    public String phoneCode;
    public String capital;
    public String currency;

    public Country(Long countryId, String countryName, String iso3, String iso2, String phoneCode, String capital, String currency) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.iso3 = iso3;
        this.iso2 = iso2;
        this.phoneCode = phoneCode;
        this.capital = capital;
        this.currency = currency;
    }
}
