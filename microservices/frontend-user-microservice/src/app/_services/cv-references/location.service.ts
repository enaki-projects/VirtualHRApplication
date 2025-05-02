import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {UserLocation} from '../../_models/cv/references/userLocation';

@Injectable({
  providedIn: 'root'
})
export class LocationService {

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

  addLocation(locationModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/locations`, locationModel).pipe(
      map((locationId: any) => {
        // console.log('[addLocation] - ' + locationId.Id);
        return locationId.Id;
      })
    );
  }

  fetchUserLocation(locationId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/locations/${locationId}`).pipe(
      map((location: UserLocation) => {
        // console.log('[fetchUserLocation] - ' + location);
        this.currentUser.cv.cvReferences.locations = this.filteredLocation(locationId);
        this.currentUser.cv.cvReferences.locations.push(location);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedLocation(locationId): void{
    this.currentUser.cv.cvReferences.locations = this.filteredLocation(locationId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredLocation(locationId: number): UserLocation[]{
    return this.currentUser.cv.cvReferences.locations.filter(item => item.locationId !== locationId);
  }

  updateLocation(locationId: number, locationModel: any): Observable<any> {
    locationModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/locations/${locationId}`, locationModel).pipe(
      map((result: any) => {
        // console.log('[updateLocation] - ' + result);
      })
    );
  }

  deleteLocation(locationId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/locations/${locationId}`).pipe(
      map((result: any) => {
        // console.log('[deleteLocation] - ' + result);
      })
    );
  }
}
