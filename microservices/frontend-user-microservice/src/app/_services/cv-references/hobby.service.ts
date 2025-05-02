import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Hobby} from '../../_models/cv/references/hobby';

@Injectable({
  providedIn: 'root'
})
export class HobbyService {

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

  addHobby(hobbyModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/hobbies`, hobbyModel).pipe(
      map((hobbyId: any) => {
        // console.log('[addHobby] - ' + hobbyId.Id);
        return hobbyId.Id;
      })
    );
  }

  fetchUserHobby(hobbyId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/hobbies/${hobbyId}`).pipe(
      map((hobby: Hobby) => {
        // console.log('[fetchUserHobby] - ' + hobby);
        this.currentUser.cv.cvReferences.hobbies = this.filteredHobby(hobbyId);
        this.currentUser.cv.cvReferences.hobbies.push(hobby);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedHobby(hobbyId): void{
    this.currentUser.cv.cvReferences.hobbies = this.filteredHobby(hobbyId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredHobby(hobbyId: number): Hobby[]{
    return this.currentUser.cv.cvReferences.hobbies.filter(item => item.hobbyId !== hobbyId);
  }

  updateHobby(hobbyId: number, hobbyModel: any): Observable<any> {
    hobbyModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/hobbies/${hobbyId}`, hobbyModel).pipe(
      map((result: any) => {
        // console.log('[updateHobby] - ' + result);
      })
    );
  }

  deleteHobby(hobbyId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/hobbies/${hobbyId}`).pipe(
      map((result: any) => {
        // console.log('[deleteHobby] - ' + result);
      })
    );
  }
}
