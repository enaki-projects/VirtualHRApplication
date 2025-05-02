import {Injectable} from '@angular/core';
import {CanActivate, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AccountService} from '../_services/account.service';
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class LoginAuthGuard implements CanActivate {
  constructor(private accountService: AccountService, private toastr: ToastrService, private router: Router) {}

  canActivate(): Observable<boolean> | boolean {
    return this.accountService.currentUser$.pipe(
      map(user => {
        if (!user) { return true; }
        this.router.navigate(['/']);
        return false;
      })
    );
  }
}
