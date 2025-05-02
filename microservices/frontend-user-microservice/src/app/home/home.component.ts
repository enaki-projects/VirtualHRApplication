import {Component, OnDestroy, OnInit} from '@angular/core';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../_services/account.service';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {Subscription} from 'rxjs';
import {environment} from '../../environments/environment';
import {AvatarService} from '../_services/avatar.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  currentUser: AuthenticatedUser;
  subscriptions: Subscription[] = [];

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    private avatarService: AvatarService
  ) {
  }

  ngOnInit(): void {
    console.log('[Home] - OnInit');
    this.avatarService.updateAvatarVideo(environment.avatarScenarios.home).subscribe((result) => {
      // console.log(result);
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
    console.log('[Home] - OnDestroy');
    this.subscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
  }
}
