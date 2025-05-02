import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PhoneNumber} from '../../_models/cv/references/phone-number';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ContactsService {
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

  addPhoneNumber(phoneModel: any): Observable<any> {
    return this.http.post(`${this.baseUrl}cv/${this.currentUser.uid}/phone-numbers`, phoneModel).pipe(
      map((phoneId: any) => {
        // console.log('[addPhoneNumber] - ' + phoneId.Id);
        return phoneId.Id;
      })
    );
  }

  fetchUserPhoneNumber(phoneNumberId: number): Observable<any>{
    return this.http.get(`${this.baseUrl}cv/phone-numbers/${phoneNumberId}`).pipe(
      map((phoneNumber: PhoneNumber) => {
        // console.log('[fetchUserPhoneNumber] - ' + phoneNumber);
        this.currentUser.cv.cvReferences.phoneNumbers = this.filteredPhoneNumber(phoneNumberId);
        this.currentUser.cv.cvReferences.phoneNumbers.push(phoneNumber);
        this.accountService.setCurrentUser(this.currentUser);
      })
    );
  }

  fetchDeletedPhoneNumber(phoneNumberId): void{
    this.currentUser.cv.cvReferences.phoneNumbers = this.filteredPhoneNumber(phoneNumberId);
    this.accountService.setCurrentUser(this.currentUser);
  }

  private filteredPhoneNumber(phoneNumberId: number): PhoneNumber[]{
    return this.currentUser.cv.cvReferences.phoneNumbers.filter(item => item.phoneNumberId !== phoneNumberId);
  }

  updatePhoneNumber(phoneId: number, phoneModel: any): Observable<any> {
    phoneModel.uid = this.currentUser.uid;
    return this.http.put(`${this.baseUrl}cv/phone-numbers/${phoneId}`, phoneModel).pipe(
      map((result: any) => {
        // console.log('[updatePhoneNumber] - ' + result);
      })
    );
  }

  deletePhoneNumber(phoneId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}cv/phone-numbers/${phoneId}`).pipe(
      map((result: any) => {
        // console.log('[deletePhoneNumber] - ' + result);
      })
    );
  }
}
