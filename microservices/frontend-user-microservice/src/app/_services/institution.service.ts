import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {EducationalInstitution} from '../_models/cv/general/educational-institution';
import {Organisation} from '../_models/cv/general/organisation';

@Injectable({
  providedIn: 'root'
})
export class InstitutionService {

  constructor(
    private http: HttpClient
  ) { }

  getBasicEducationalInstitutionsByCountryId(countryId: number): Promise<any>{
    const path = `${environment.adapterApiUrl}institutions/educational-by-country/${countryId}`;
    return this.http.get<EducationalInstitution[]>(path).pipe(
      map((educationalInstitutions: EducationalInstitution[]) => {
        // console.log('[InstitutionService Service] - Fetching educational institutions by country id');
        // console.log(educationalInstitutions);
        return educationalInstitutions;
      })
    ).toPromise();
  }

  getBasicProfessionalInstitutionsByCityId(cityId: number): Promise<any> {
    const path = `${environment.adapterApiUrl}organisations-by-city/${cityId}`;
    return this.http.get<Organisation[]>(path).pipe(
      map((organisations: Organisation[]) => {
        // console.log('[InstitutionService Service] - Fetching companies by city id');
        // console.log(organisations);
        return organisations;
      })
    ).toPromise();
  }
}
