import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {AccountService} from '../_services/account.service';
import {ToastrService} from 'ngx-toastr';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class IqAccessGuard implements CanActivate {
  constructor(
    private accountService: AccountService,
    private toastr: ToastrService,
    private router: Router) {}

  canActivate(): Observable<boolean> | boolean {
    return this.accountService.currentUser$.pipe(
      map(user => {
        if (user && user.cv.canTakeTheIqRequest) { return true; }
        this.toastr.info('You have already taken the test.');
        this.router.navigate(['iq-test']);
        return false;
      })
    );
  }
}
