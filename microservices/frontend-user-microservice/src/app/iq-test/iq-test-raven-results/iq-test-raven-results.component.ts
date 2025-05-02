import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../../_services/account.service';
import {AvatarService} from '../../_services/avatar.service';
import {environment} from '../../../environments/environment';
import {IqService} from '../../_services/iq/iq.service';
import {RavenReview} from '../../_models/iq/raven-apm/raven-review';

@Component({
  selector: 'app-iq-test-raven-results',
  templateUrl: './iq-test-raven-results.component.html',
  styleUrls: ['./iq-test-raven-results.component.css']
})
export class IqTestRavenResultsComponent implements OnInit, OnDestroy {

  currentUser: AuthenticatedUser;
  subscriptions: Subscription[] = [];
  review: RavenReview;

  constructor(
    private iqService: IqService,
    private toastr: ToastrService,
    private accountService: AccountService,
    public avatarService: AvatarService
  ) { }

  ngOnInit(): void {
    console.log('[IQTest] - OnInit');
    this.avatarService.updateAvatarVideo(environment.avatarScenarios.iq_test_analysis).subscribe((result) => {
      // console.log(result);
    });
    this.iqService.getLatestUserIqQuizResponse().toPromise().then((review) => {
      this.review = review;
    });
    this.loadCurrentUser();
  }

  loadCurrentUser(): void {
    const subscription = this.accountService.currentUser$.subscribe(currentUser => {
      this.currentUser = currentUser;
    });
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    console.log('[IQTest] - OnDestroy');
    this.subscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
  }

  requestToRetakeTheTest(): void {
    this.iqService.requestToRetakeTheTest().toPromise().then((response) => {
      // console.log(response);
      this.toastr.info('You have requested the iq test');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  getTimeInMinutes(timeInSeconds: number): string{
    return `${Math.floor(timeInSeconds / 60)} minutes ${timeInSeconds % 60} seconds`;
  }
}
