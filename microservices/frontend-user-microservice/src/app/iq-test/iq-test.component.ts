import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../_services/account.service';
import {AvatarService} from '../_services/avatar.service';
import {environment} from '../../environments/environment';

@Component({
  selector: 'app-iq-test',
  templateUrl: './iq-test.component.html',
  styleUrls: ['./iq-test.component.css']
})
export class IqTestComponent implements OnInit, OnDestroy {

  currentUser: AuthenticatedUser;
  subscriptions: Subscription[] = [];

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    public avatarService: AvatarService,
  ) { }

  ngOnInit(): void {
    console.log('[IQTest] - OnInit');
    this.loadCurrentUser();
  }

  loadCurrentUser(): void {
    const subscription = this.accountService.currentUser$.subscribe(currentUser => {
      this.currentUser = currentUser;
      let scenario = environment.avatarScenarios.iq_test;
      if (this.currentUser && !this.currentUser.cv.canTakeTheIqRequest){
        scenario = environment.avatarScenarios.iq_test_result;
      }
      this.avatarService.updateAvatarVideo(scenario).subscribe((result) => {
        // console.log(result);
      });
    });
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    console.log('[IQTest] - OnDestroy');
    this.subscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
  }

}
