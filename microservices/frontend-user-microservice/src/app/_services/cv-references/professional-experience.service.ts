import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {ProfessionalExperience} from '../../_models/cv/references/professional-experience';

@Injectable({
  providedIn: 'root'
})
export class ProfessionalExperienceService {

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

  addProfessionalExperience(professionalExperienceModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/professional-experiences`, professionalExperienceModel).pipe(
      map((professionalExperienceId: any) => {
        // console.log('[addProfessionalExperience] - ' + professionalExperienceId.Id);
        return professionalExperienceId.Id;
      })
    );
  }

  fetchUserProfessionalExperience(professionalExperienceId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/professional-experiences/${professionalExperienceId}`).pipe(
      map((professionalExperience: ProfessionalExperience) => {
        // console.log('[fetchUserProfessionalExperience] - ' + professionalExperience);
        this.currentUser.cv.cvReferences.professionalExperiences = this.filteredProfessionalExperience(professionalExperienceId);
        this.currentUser.cv.cvReferences.professionalExperiences.push(professionalExperience);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedProfessionalExperience(professionalExperienceId): void{
    this.currentUser.cv.cvReferences.professionalExperiences = this.filteredProfessionalExperience(professionalExperienceId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredProfessionalExperience(professionalExperienceId: number): ProfessionalExperience[]{
    return this.currentUser.cv.cvReferences.professionalExperiences.filter(item => item.professionalExperienceId !== professionalExperienceId);
  }

  updateProfessionalExperience(professionalExperienceId: number, professionalExperienceModel: any): Observable<any> {
    professionalExperienceModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/professional-experiences/${professionalExperienceId}`, professionalExperienceModel).pipe(
      map((result: any) => {
        // console.log('[updateProfessionalExperience] - ' + result);
      })
    );
  }

  deleteProfessionalExperience(professionalExperienceId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/professional-experiences/${professionalExperienceId}`).pipe(
      map((result: any) => {
        // console.log('[deleteProfessionalExperience] - ' + result);
      })
    );
  }
}
