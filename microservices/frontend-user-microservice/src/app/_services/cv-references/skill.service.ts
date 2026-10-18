import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {UserSkill} from '../../_models/cv/references/user-skill';

@Injectable({
  providedIn: 'root'
})
export class SkillService {
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

  addSkill(skillModel: any, skillType): Observable<any> {

    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/${skillType}?name=${encodeURIComponent(skillModel.name)}`, {}).pipe(
      map((result: any) => {
        // console.log('[addSkill] - ' + result);
      })
    );
  }

  deleteSkill(skillModel: any, skillType): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/${this.currentUser.uid}/${skillType}?name=${encodeURIComponent(skillModel.name)}`).pipe(
      map((result: any) => {
        // console.log('[deleteSkill] - ' + result);
      })
    );
  }

  fetchUserSkill(skill: any, skillType: string): Observable<any> {
    return this.http.get(`${this.baseUrl}cv/skill-by-name/${skillType}/${encodeURIComponent(skill.name)}`).pipe(
      map((userSkill: UserSkill) => {
        // console.log('[fetchUserSkill] - ' + userSkill);
        this.currentUser.cv.cvReferences[`user${this.toTitleCase(skillType)}Skills`].push(userSkill);
        this.accountService.setCurrentUser(this.currentUser);
        return userSkill;
      })
    );
  }

  toTitleCase(str: string): string {
    return str.replace(
      /\w\S*/g,
      (txt) => txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase()
    );
  }

  fetchDeletedUserSkill(skill: UserSkill, skillType: string): void {
    this.currentUser.cv.cvReferences[`user${this.toTitleCase(skillType)}Skills`] = this.filteredSkill(skill.skillId, skillType);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredSkill(skillId: any, skillType: string): UserSkill[] {
    return this.currentUser.cv.cvReferences[`user${this.toTitleCase(skillType)}Skills`].filter(item => item.skillId !== skillId);
  }

  getSkills(skillType: string): Promise<any>{
    const path = `${environment.adapterApiUrl}cv/skills/${skillType}`;
    return this.http.get<UserSkill[]>(path).pipe(
      map((userSkills: UserSkill[]) => {
        // console.log('[Skill Service] - Fetching skills');
        // console.log(userSkills);
        return userSkills;
      })
    ).toPromise();
  }
}
