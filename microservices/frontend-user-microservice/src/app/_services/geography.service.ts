import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {Country} from '../_models/cv/general/country';
import {City} from '../_models/cv/general/city';
import {Region} from '../_models/cv/general/region';
import {Language} from '../_models/cv/general/language';
import {Citizenship} from '../_models/cv/references/citizenship';

@Injectable({
  providedIn: 'root'
})
export class GeographyService {

  constructor(
    private http: HttpClient
  ) { }

  getCountries(): Promise<Country[]>{
    const path = `${environment.adapterApiUrl}geography/countries`;
    const storageCountries = JSON.parse(localStorage.getItem('countries'));
    if (storageCountries !== undefined && storageCountries !== null){
      return Promise.resolve(storageCountries.data);
    } else {
      return this.http.get<Country[]>(path).pipe(
        map((countries: Country[]) => {
          console.log('[Geography Service] - Fetching countries');
          // console.log(countries);
          const toSave = {
            data: countries,
            time: new Date().getTime()
          };
          localStorage.setItem('countries', JSON.stringify(toSave));
          return countries;
        })
      ).toPromise();
    }
  }

  getCities(): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/cities`;
    return this.http.get<City[]>(path).pipe(
      map((cities: City[]) => {
        console.log('[City Service] - Fetching cities');
        // console.log(cities);
        return cities;
      })
    ).toPromise();
  }

  getCitiesByRegionId(regionId: number): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/regions/${regionId}/cities`;
    return this.http.get<City[]>(path).pipe(
      map((cities: City[]) => {
        console.log('[City Service] - Fetching cities by region id');
        // console.log(cities);
        return cities;
      })
    ).toPromise();
  }

  getCitiesByCountryId(countryId: number): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/countries/${countryId}/cities`;
    return this.http.get<City[]>(path).pipe(
      map((cities: City[]) => {
        console.log('[Country Service] - Fetching cities by country id');
        // console.log(cities);
        return cities;
      })
    ).toPromise();
  }

  getRegionsByCountryIdWhichHaveCities(regionId: number): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/countries/${regionId}/regions?whichHaveCities=1`;
    return this.http.get<Region[]>(path).pipe(
      map((regions: Region[]) => {
        console.log('[Country Service] - Fetching regions by country id which have cities');
        // console.log(regions);
        return regions;
      })
    ).toPromise();
  }

  getCountriesWhichHaveCities(): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/countries?whichHaveCities=1`;
    return this.http.get<Country[]>(path).pipe(
      map((regions: Country[]) => {
        console.log('[Country Service] - Fetching countries which have cities');
        // console.log(regions);
        return regions;
      })
    ).toPromise();
  }

  getCountriesWhichHaveUniversities(): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/countries?whichHaveUniversities=1`;
    return this.http.get<Country[]>(path).pipe(
      map((regions: Country[]) => {
        console.log('[Country Service] - Fetching countries which have cities');
        // console.log(regions);
        return regions;
      })
    ).toPromise();
  }

  getRegions(): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/regions`;
    const storageCities = JSON.parse(localStorage.getItem('regions'));
    if (storageCities !== undefined && storageCities !== null){
      return Promise.resolve(storageCities.data);
    } else {
      return this.http.get<Region[]>(path).pipe(
        map((regions: Region[]) => {
          console.log('[Region Service] - Fetching regions');
          //console.log(regions);
          const toSave = {
            data: regions,
            time: new Date().getTime()
          };
          localStorage.setItem('regions', JSON.stringify(toSave));
          return regions;
        })
      ).toPromise();
    }
  }

  getLanguages(): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/languages`;
    const storageLanguages = JSON.parse(localStorage.getItem('languages'));
    if (storageLanguages !== undefined && storageLanguages !== null){
      return Promise.resolve(storageLanguages.data);
    } else {
      return this.http.get<Language[]>(path).pipe(
        map((languages: Language[]) => {
          console.log('[Language Service] - Fetching languages');
          // console.log(languages);
          const toSave = {
            data: languages,
            time: new Date().getTime()
          };
          localStorage.setItem('languages', JSON.stringify(toSave));
          return languages;
        })
      ).toPromise();
    }
  }

  getNationalities(): Promise<any>{
    const path = `${environment.adapterApiUrl}geography/nationalities`;
    const storageNationalities = JSON.parse(localStorage.getItem('nationalities'));
    if (storageNationalities !== undefined && storageNationalities !== null){
      return Promise.resolve(storageNationalities.data);
    } else {
      return this.http.get<Citizenship[]>(path).pipe(
        map((nationalities: Citizenship[]) => {
          console.log('[Nationality Service] - Fetching nationalities');
          // console.log(nationalities);
          const toSave = {
            data: nationalities,
            time: new Date().getTime()
          };
          localStorage.setItem('nationalities', JSON.stringify(toSave));
          return nationalities;
        })
      ).toPromise();
    }
  }
}
