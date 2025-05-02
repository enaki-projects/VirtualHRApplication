import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Citizenship} from '../../_models/cv/references/citizenship';

@Injectable({
  providedIn: 'root'
})
export class CitizenshipService {
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

  fetchUserCitizenship(nationalityId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}geography/nationalities/${nationalityId}`).pipe(
      map((citizenship: Citizenship) => {
        // console.log('[fetchUserCitizenship] - ' + citizenship);
        this.currentUser.cv.cvReferences.citizenship.push(citizenship);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedCitizenship(nationalityId): void{
    this.currentUser.cv.cvReferences.citizenship = this.filteredCitizenship(nationalityId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredCitizenship(nationalityId: number): Citizenship[]{
    return this.currentUser.cv.cvReferences.citizenship.filter(item => item.nationalityId !== nationalityId);
  }

  addCitizenship(nationalityId: number): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/citizenship/${nationalityId}`, null).pipe(
      map((result: any) => {
        // console.log('[addCitizenship] - ' + result);
      })
    );
  }

  deleteCitizenship(nationalityId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/${this.currentUser.uid}/citizenship/${nationalityId}`).pipe(
      map((result: any) => {
        // console.log('[deleteCitizenship] - ' + result);
      })
    );
  }
}
