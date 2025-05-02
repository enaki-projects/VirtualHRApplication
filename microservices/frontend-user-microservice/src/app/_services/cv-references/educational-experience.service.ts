import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {EducationalExperience} from '../../_models/cv/references/educational-experience';

@Injectable({
  providedIn: 'root'
})
export class EducationalExperienceService {

  currentUser: AuthenticatedUser;
  baseUrl = environment.adapterApiUrl;

  constructor(
    private accountService: AccountService,
    private http: HttpClient
  ) {
    accountService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  addEducationalExperience(educationalExperienceModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/educational-experiences`, educationalExperienceModel).pipe(
      map((educationalExperienceId: any) => {
        // console.log('[addEducationalExperience] - ' + educationalExperienceId.Id);
        return educationalExperienceId.Id;
      })
    );
  }

  fetchUserEducationalExperience(educationalExperienceId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/educational-experiences/${educationalExperienceId}`).pipe(
      map((educationalExperience: EducationalExperience) => {
        // console.log('[fetchUserEducationalExperience] - ' + educationalExperience);
        this.currentUser.cv.cvReferences.educationalExperiences = this.filteredEducationalExperience(educationalExperienceId);
        this.currentUser.cv.cvReferences.educationalExperiences.push(educationalExperience);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedEducationalExperience(educationalExperienceId): void{
    this.currentUser.cv.cvReferences.educationalExperiences = this.filteredEducationalExperience(educationalExperienceId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredEducationalExperience(educationalExperienceId: number): EducationalExperience[]{
    return this.currentUser.cv.cvReferences.educationalExperiences.filter(item => item.educationalExperienceId !== educationalExperienceId);
  }

  updateEducationalExperience(educationalExperienceId: number, educationalExperienceModel: any): Observable<any> {
    educationalExperienceModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/educational-experiences/${educationalExperienceId}`, educationalExperienceModel).pipe(
      map((result: any) => {
        // console.log('[updateEducationalExperience] - ' + result);
      })
    );
  }

  deleteEducationalExperience(educationalExperienceId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/educational-experiences/${educationalExperienceId}`).pipe(
      map((result: any) => {
        // console.log('[deleteEducationalExperience] - ' + result);
      })
    );
  }
}
