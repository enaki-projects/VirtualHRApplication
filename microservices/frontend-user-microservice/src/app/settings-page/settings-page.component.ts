import { Component, OnDestroy, OnInit } from '@angular/core';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../_services/account.service';
import {AvatarService} from '../_services/avatar.service';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {faKey, faLock, faUser} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-settings-page',
  templateUrl: './settings-page.component.html',
  styleUrls: ['./settings-page.component.css']
})
export class SettingsPageComponent implements OnInit, OnDestroy {

  currentUser: AuthenticatedUser;
  subscriptions: Subscription[] = [];
  avatarSettingsForm: FormGroup;
  passwordChangeForm: FormGroup;
  faKey = faKey;
  faLock = faLock;
  faUser = faUser;

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    private avatarService: AvatarService,
    private fb: FormBuilder,
  ) {
    this.avatarService.avatarSettings$.subscribe(settings => {
      this.avatarSettingsForm = this.fb.group({
        resolution: [settings.resolution, [Validators.required]],
        language: [settings.language, [Validators.required]],
        enabled: [(settings.enabled) ? '1' : '0', [Validators.required]],
        enableSubtitles: [(settings.enableSubtitles) ? '1' : '0', [Validators.required]],
      });
    });
  }

  ngOnInit(): void {
    console.log('[Settings] - OnInit');
    this.loadCurrentUser();
    this.initializeForm();
  }

  matchValues(matchTo: string): ValidatorFn {
    return (control: AbstractControl) => {
      return control?.value === control?.parent?.controls[matchTo].value
        ? null : {isMatching: true};
    };
  }

  initializeForm(): void {
    this.passwordChangeForm = this.fb.group({
      email: [this.currentUser.email, [Validators.required, Validators.minLength(6)]],
      oldPassword: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      passwordConfirm: ['',  [Validators.required, this.matchValues('password')]],
    });
    this.passwordChangeForm.controls.email.disable();
  }

  loadCurrentUser(): void {
    const subscription = this.accountService.currentUser$.subscribe(currentUser => {
      this.currentUser = currentUser;
    });
    this.subscriptions.push(subscription);
  }

  updateSettings(): void{
    const resolution = this.avatarSettingsForm.get('resolution').value;
    const language = this.avatarSettingsForm.get('language').value;
    const enabled = this.avatarSettingsForm.get('enabled').value === '1';
    const enableSubtitles = this.avatarSettingsForm.get('enableSubtitles').value === '1';
    this.avatarService.updateSettings(language, resolution, enabled, enableSubtitles);
    this.toastr.success('Avatar Settings updated successfully');
  }

  updatePassword(): void{
    this.accountService.changePassword(this.passwordChangeForm.get('oldPassword').value,
      this.passwordChangeForm.get('password').value).subscribe(response => {
      console.log(response);
      this.toastr.success('Successfully Changed');
      this.initializeForm();
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  ngOnDestroy(): void {
    console.log('[Settings] - OnDestroy');
    this.subscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
  }

}
