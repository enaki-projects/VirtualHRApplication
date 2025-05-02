package org.enaki.license.cv_db.business.business_models.get.geography;

import org.enaki.license.cv_db.persistence.entities.geography.Country;

import java.util.List;
import java.util.stream.Collectors;

public class BusinessCountry {
    public Long countryId;
    public String countryName;
    public String iso3;
    public String iso2;
    public String phoneCode;
    public String capital;
    public String currency;

    public BusinessCountry(Long countryId, String countryName, String iso3, String iso2, String phoneCode, String capital, String currency) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.iso3 = iso3;
        this.iso2 = iso2;
        this.phoneCode = phoneCode;
        this.capital = capital;
        this.currency = currency;
    }

    public static BusinessCountry toBusinessCountry(Country country) {
        return new BusinessCountry(
                country.countryId, country.countryName, country.iso3, country.iso2, country.phoneCode, country.capital, country.currency
        );
    }

    public static List<BusinessCountry> toBusinessCountryList(List<Country> countryList) {
        return countryList.stream()
                .map(BusinessCountry::toBusinessCountry)
                .collect(Collectors.toList());
    }
}
