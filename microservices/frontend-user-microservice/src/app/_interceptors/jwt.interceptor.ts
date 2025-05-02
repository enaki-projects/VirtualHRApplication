import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AccountService} from '../_services/account.service';
import {take, tap} from 'rxjs/operators';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {ToastrService} from 'ngx-toastr';
import {Router} from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

  constructor(private accountService: AccountService,
              private toastr: ToastrService,
              private router: Router) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    let currentUser: AuthenticatedUser;

    this.accountService.currentUser$.pipe(take(1)).subscribe(user => currentUser = user);
    if (currentUser) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
    }
    return next.handle(request).pipe(tap((event: HttpEvent<any>) => {
      if (event instanceof HttpResponse) {
        // for email confirmation
        if (event.status === 202){
          this.toastr.info('Please Check Your Email');
        }
      }
    }, (err: any) => {
      if (err instanceof HttpErrorResponse) {
        if (err.status === 401) {
          if (!this.router.url.includes('auth/login')) {
            this.toastr.info('Your session token has expired. Please login again.');
          }
          this.accountService.logout();
          this.router.navigateByUrl('/auth/login');
        }
      }
    }));
  }
}
