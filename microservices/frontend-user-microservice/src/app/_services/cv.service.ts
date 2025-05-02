import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {AccountService} from './account.service';
import {UserSkill} from '../_models/cv/references/user-skill';
import {map} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';
import {Citizenship} from '../_models/cv/references/citizenship';
import {Hobby} from '../_models/cv/references/hobby';

@Injectable({
  providedIn: 'root'
})
export class CvService {
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

  getSkillsUnited(skills: UserSkill[]): string{
    return skills.map((skill) => skill.name).join(', ');
  }

  hasAnySkills(): boolean {
    return this.currentUser.cv.cvReferences.userInterpersonalSkills.length > 0 ||
      this.currentUser.cv.cvReferences.userDigitalSkills.length > 0 ||
      this.currentUser.cv.cvReferences.userOrganisationalSkills.length > 0 ||
      this.currentUser.cv.cvReferences.userLeadershipSkills.length > 0;
  }

  getCitizenshipUnited(nationalities: Citizenship[]): string{
    return nationalities.map((nationality) => nationality.nationalityName).join(', ');
  }

  getHobbiesUnited(hobbies: Hobby[]): string{
    return hobbies.map((hobby) => this.toTitleCase(hobby.name)).join(', ');
  }

  private toTitleCase(str: string): string {
    return str.replace(
      /\w\S*/g,
      (txt) => txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase()
    );
  }

  updateGeneralInfo(model: any): Observable<any>{
    // fixing local zone
    model.birthdate = new Date(
      model.birthdate.getFullYear(),
      model.birthdate.getMonth(),
      model.birthdate.getDate(),
      model.birthdate.getHours(),
      model.birthdate.getMinutes(),
      model.birthdate.getSeconds()
    );

    return this.http.put(`${this.baseUrl}cvs/${this.currentUser.uid}`, model).pipe(
      map((response: any) => {
        // console.log(response);
        this.currentUser.cv.firstname = model.firstname;
        this.currentUser.cv.lastname = model.lastname;
        this.currentUser.cv.birthdate = model.birthdate;
        this.currentUser.cv.gender = model.gender;
        this.currentUser.cv.email = model.email;
        this.currentUser.cv.motto = model.motto;
        this.currentUser.cv.aboutMe = model.aboutMe;
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }


}
