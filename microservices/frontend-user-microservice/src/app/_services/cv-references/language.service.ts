import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {UserLanguage} from '../../_models/cv/references/user-language';

@Injectable({
  providedIn: 'root'
})
export class LanguageService {

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

  addLanguage(languageModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/user-languages`, languageModel).pipe(
      map((languageId: any) => {
        // console.log('[addLanguage] - ' + languageId.Id);
        return languageId.Id;
      })
    );
  }

  fetchUserLanguage(languageId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/user-languages/${languageId}`).pipe(
      map((language: UserLanguage) => {
        // console.log('[fetchUserLanguage] - ' + language);
        this.currentUser.cv.cvReferences.userLanguages = this.filteredLanguage(languageId);
        this.currentUser.cv.cvReferences.userLanguages.push(language);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedLanguage(languageId): void{
    this.currentUser.cv.cvReferences.userLanguages = this.filteredLanguage(languageId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredLanguage(languageId: number): UserLanguage[]{
    return this.currentUser.cv.cvReferences.userLanguages.filter(item => item.userLanguageId !== languageId);
  }

  updateLanguage(languageId: number, languageModel: any): Observable<any> {
    languageModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/user-languages/${languageId}`, languageModel).pipe(
      map((result: any) => {
        // console.log('[updateLanguage] - ' + result);
      })
    );
  }

  deleteLanguage(languageId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/user-languages/${languageId}`).pipe(
      map((result: any) => {
        // console.log('[deleteLanguage] - ' + result);
      })
    );
  }

}
