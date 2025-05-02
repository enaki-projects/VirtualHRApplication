import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Award} from '../../_models/cv/references/award';

@Injectable({
  providedIn: 'root'
})
export class AwardService {

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

  addAward(awardModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/awards`, awardModel).pipe(
      map((awardId: any) => {
        // console.log('[addAward] - ' + awardId.Id);
        return awardId.Id;
      })
    );
  }

  fetchUserAward(awardId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/awards/${awardId}`).pipe(
      map((award: Award) => {
        // console.log('[fetchUserAward] - ' + award);
        this.currentUser.cv.cvReferences.awards = this.filteredAward(awardId);
        this.currentUser.cv.cvReferences.awards.push(award);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedAward(awardId): void{
    this.currentUser.cv.cvReferences.awards = this.filteredAward(awardId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredAward(awardId: number): Award[]{
    return this.currentUser.cv.cvReferences.awards.filter(item => item.awardId !== awardId);
  }

  updateAward(awardId: number, awardModel: any): Observable<any> {
    awardModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/awards/${awardId}`, awardModel).pipe(
      map((result: any) => {
        // console.log('[updateAward] - ' + result);
      })
    );
  }

  deleteAward(awardId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/awards/${awardId}`).pipe(
      map((result: any) => {
        // console.log('[deleteAward] - ' + result);
      })
    );
  }
}
