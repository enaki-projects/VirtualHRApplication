import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../_services/account.service';
import {AvatarService} from '../_services/avatar.service';
import {environment} from '../../environments/environment';

@Component({
  standalone: false,
  selector: 'app-cv-editor',
  templateUrl: './cv-editor.component.html',
  styleUrls: ['./cv-editor.component.css']
})
export class CvEditorComponent implements OnInit, OnDestroy {
  currentUser: AuthenticatedUser;
  subscriptions: Subscription[] = [];
  tabIndex = 0;

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    public avatarService: AvatarService
  ) {
  }

  ngOnInit(): void {
    console.log('[CV] - OnInit');
    this.avatarService.updateAvatarVideo(environment.avatarScenarios.cv_page).toPromise().then(() => {});
    this.loadCurrentUser();
  }

  loadCurrentUser(): void {
    const subscription = this.accountService.currentUser$.subscribe(currentUser => {
      this.currentUser = currentUser;
    });
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    console.log('[CV] - OnDestroy');
    this.subscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
  }

  onTabClick(idx: number): void {
    this.tabIndex = idx;
    let scenario;
    switch (this.tabIndex){
      case 0:
        scenario = environment.avatarScenarios.cv_personal_info;
        break;
      case 1:
        scenario = environment.avatarScenarios.cv_contact;
        break;
      case 2:
        scenario = environment.avatarScenarios.cv_nationality;
        break;
      case 3:
        scenario = environment.avatarScenarios.cv_language;
        break;
      case 4:
        scenario = environment.avatarScenarios.cv_location;
        break;
      case 5:
        scenario = environment.avatarScenarios.cv_hobby;
        break;
      case 6:
        scenario = environment.avatarScenarios.cv_projects;
        break;
      case 7:
        scenario = environment.avatarScenarios.cv_publications;
        break;
      case 8:
        scenario = environment.avatarScenarios.cv_awards;
        break;
      case 9:
        scenario = environment.avatarScenarios.cv_educational_experiences;
        break;
      case 10:
        scenario = environment.avatarScenarios.cv_professional_experiences;
        break;
      case 11:
        scenario = environment.avatarScenarios.cv_skills;
        break;
    }
    if (scenario){
      this.avatarService.updateAvatarVideo(scenario).toPromise().then(() => {});
    }

  }
}
