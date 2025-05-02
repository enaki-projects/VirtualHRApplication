import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {BusyService} from '../_services/busy.service';
import {delay, finalize} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable()
export class LoadingInterceptor implements HttpInterceptor {

  constructor(private busyService: BusyService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    if (request.method === 'GET'){
      if (request.url.includes(environment.avatarApiUrl)){
        return next.handle(request);
      }
    }
    this.busyService.busy();
    return next.handle(request).pipe(
      /*delay(1000),*/
      finalize(() => {
        this.busyService.idle();
      })
    );
  }
}
