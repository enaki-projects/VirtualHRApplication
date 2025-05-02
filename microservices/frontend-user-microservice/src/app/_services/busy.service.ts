import {Injectable} from '@angular/core';
import {NgxSpinnerService} from 'ngx-spinner';

@Injectable({
  providedIn: 'root'
})
export class BusyService {
  busyRequestCount = 0;

  constructor(private spinnerService: NgxSpinnerService) { }

  busy(): void {
    this.busyRequestCount++;
    this.spinnerService.show(undefined, {
      type: 'ball-atom',
      bdColor: 'rgba(160,146,146,0.3)',
      color: '#000000'
    });
  }

  idle(): void {
    this.busyRequestCount--;
    if (this.busyRequestCount <= 0) {
      this.busyRequestCount = 0;
      this.spinnerService.hide();
    }
  }
}
