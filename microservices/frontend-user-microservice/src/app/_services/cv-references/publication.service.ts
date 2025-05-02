import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Publication} from '../../_models/cv/references/publication';

@Injectable({
  providedIn: 'root'
})
export class PublicationService {

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

  addPublication(publicationModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/publications`, publicationModel).pipe(
      map((publicationId: any) => {
        // console.log('[addPublication] - ' + publicationId.Id);
        return publicationId.Id;
      })
    );
  }

  fetchUserPublication(publicationId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/publications/${publicationId}`).pipe(
      map((publication: Publication) => {
        // console.log('[fetchUserPublication] - ' + publication);
        this.currentUser.cv.cvReferences.publications = this.filteredPublication(publicationId);
        this.currentUser.cv.cvReferences.publications.push(publication);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedPublication(publicationId): void{
    this.currentUser.cv.cvReferences.publications = this.filteredPublication(publicationId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredPublication(publicationId: number): Publication[]{
    return this.currentUser.cv.cvReferences.publications.filter(item => item.publicationId !== publicationId);
  }

  updatePublication(publicationId: number, publicationModel: any): Observable<any> {
    publicationModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/publications/${publicationId}`, publicationModel).pipe(
      map((result: any) => {
        // console.log('[updatePublication] - ' + result);
      })
    );
  }

  deletePublication(publicationId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/publications/${publicationId}`).pipe(
      map((result: any) => {
        // console.log('[deletePublication] - ' + result);
      })
    );
  }
}
