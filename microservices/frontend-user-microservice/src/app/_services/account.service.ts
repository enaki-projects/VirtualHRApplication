import {Injectable} from '@angular/core';
import {Observable, ReplaySubject} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map} from 'rxjs/operators';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {CV} from '../_models/cv/cv';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constructor(private http: HttpClient) {
    this.currentUserSource.next(JSON.parse(localStorage.getItem('user')) || null);
  }
  baseUrl = environment.adapterApiUrl;
  private currentUserSource = new ReplaySubject<AuthenticatedUser>(1);
  currentUser$ = this.currentUserSource.asObservable();
  private timeStamp: number;

  private static getUserImageAccordingToGender(gender: string): string{
    switch (gender.toLowerCase()) {
      case 'male':
        return `./assets/images/male.png`;
      case 'female':
        return  `./assets/images/female.png`;
      default:
        return  `./assets/images/user.png`;
    }
  }

  login(model: any): Observable<any> {
    return this.http.post(this.baseUrl + 'auth/login', model).pipe(
      map((response: AuthenticatedUser) => {
        const user = response;
        if (user) {
          localStorage.setItem('user', JSON.stringify(user));
          this.currentUserSource.next(user);
        }
      })
    );
  }

  register(model: any): Observable<any> {
    // fixing local zone

    model.birthdate = new Date(
      model.birthdate.getFullYear(),
      model.birthdate.getMonth(),
      model.birthdate.getDate(),
      model.birthdate.getHours(),
      model.birthdate.getMinutes(),
      model.birthdate.getSeconds()
    );

    return this.http.post(this.baseUrl + 'auth/register', model);
  }

  changePassword(oldPassword: string, password: string): Observable<any> {
    console.log(oldPassword);
    console.log(password);
    const model = {
      oldPassword,
      password
    };
    return this.http.put(`${this.baseUrl}users/${this.getCurrentUser().uid}/change-password`, model);
  }

  getUserPhotoUrl(): string{
    if (this.timeStamp) {
      return this.getCurrentUser().urlImage + '?' + this.timeStamp;
    }
    return this.getCurrentUser().urlImage;
  }

  refreshUserImage(): void{
    const currentUser = this.getCurrentUser();
    currentUser.cv.hasImage = true;
    currentUser.urlImage = `${this.baseUrl}users/${currentUser.uid}/image`;
    this.setCurrentUser(currentUser);
    this.timeStamp = (new Date()).getTime();
  }

  fetchUserCv(): Observable<any>{
    const uid = this.getCurrentUser().uid;
    return this.http.get(`${this.baseUrl}cvs/${uid}`).pipe(
      map((cv: CV) => {
        const currentUser = this.getCurrentUser();
        currentUser.cv = cv;
        currentUser.cv.birthdate = new Date(currentUser.cv.birthdate);
        if (currentUser.cv.hasImage){
          currentUser.urlImage = `${this.baseUrl}users/${currentUser.uid}/image`;
        } else {
          currentUser.urlImage = AccountService.getUserImageAccordingToGender(currentUser.cv.gender);
        }
        this.setCurrentUser(currentUser);
      })
    );
  }

  setCurrentUser(user: AuthenticatedUser): void {
    if (!user.cv.hasImage){
      user.urlImage = AccountService.getUserImageAccordingToGender(user.cv.gender);
    }
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSource.next(user);
  }

  getCurrentUser(): AuthenticatedUser {
    return JSON.parse(localStorage.getItem('user')) || null;
  }

  logout(): void {
    localStorage.removeItem('user');
    this.currentUserSource.next(null);
  }

  deleteUserPhoto(): Observable<any> {
    return this.http.delete(this.getCurrentUser().urlImage).pipe(map(() => {
      const currentUser = this.getCurrentUser();
      currentUser.cv.hasImage = false;
      currentUser.urlImage = AccountService.getUserImageAccordingToGender(currentUser.cv.gender);
      this.setCurrentUser(currentUser);
    }));
  }

  requestRecover(email: string): Observable<any> {
    return this.http.post(this.baseUrl + 'auth/recover', {email});
  }

  confirmRecover(token: string, password: string): Observable<any> {
    return this.http.post(this.baseUrl + 'auth/recover/confirm/' + token, {password});
  }
}
