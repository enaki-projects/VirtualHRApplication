import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {ConfirmService} from '../_services/confirm/confirm.service';

@Injectable({
  providedIn: 'root'
})
export class AccessTestGuard implements CanActivate {
  constructor(private confirmService: ConfirmService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      return this.confirmService.confirm('Confirmation',
        'Are you sure you want to access this test?',
        ['You can take this test only once.',
          'You can\'t return to answered questions.',
          'You can\'t leave the test once you start it.',
        ],
        'Yes',
        'No',
        );
  }
}
