import {Injectable} from '@angular/core';
import {CanDeactivate} from '@angular/router';
import {ConfirmService} from '../_services/confirm/confirm.service';
import {IqTestRavenComponent} from '../iq-test/iq-test-raven/iq-test-raven.component';
import {IqService} from '../_services/iq/iq.service';

@Injectable({
  providedIn: 'root'
})
export class LeaveTestGuard implements CanDeactivate<unknown> {
  private hasStartedTheTest = false;
  constructor(private confirmService: ConfirmService,
              private iqService: IqService
              ) {
    iqService.hasStartedTheTest$.subscribe(hasStartedTheTest => {
      this.hasStartedTheTest = hasStartedTheTest;
    });
  }

  async canDeactivate(component: IqTestRavenComponent): Promise<boolean> {
    if (!this.hasStartedTheTest){
      return true;
    }
    const answer: boolean = await this.confirmService.confirm('Confirmation',
      'Are you sure you want to submit and close the test?').toPromise();
    if (answer){
      component.submitIqResponses();
      return true;
    }
    return false;
  }
}
