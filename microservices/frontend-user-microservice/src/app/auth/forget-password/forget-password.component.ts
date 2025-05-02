import {Component, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {AccountService} from '../../_services/account.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {faKey, faUser} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-forget-password',
  templateUrl: './forget-password.component.html',
  styleUrls: ['./forget-password.component.css']
})
export class ForgetPasswordComponent implements OnInit {
  faUser = faUser;
  faKey = faKey;
  model: any = {};
  recoverForm: FormGroup;
  confirmRecoverForm: FormGroup;
  token: string;

  constructor(
    public accountService: AccountService,
    private router: Router,
    private toastr: ToastrService,
    private fb: FormBuilder,
    private activatedRoute: ActivatedRoute
  ) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.token = params.token;
      console.log(this.token);
    });
  }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.recoverForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
    this.confirmRecoverForm = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(6)]],
      passwordConfirm: ['',  [Validators.required, this.matchValues('password')]],
    });
  }

  matchValues(matchTo: string): ValidatorFn {
    return (control: AbstractControl) => {
      return control?.value === control?.parent?.controls[matchTo].value
        ? null : {isMatching: true};
    };
  }

  sendRequestRecover(): void {
    this.accountService.requestRecover(this.recoverForm.get('email').value).subscribe(response => {
      console.log(response);
      this.router.navigateByUrl('/auth/login');
      this.toastr.success('Requested The Recover');

    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  confirmRecover(): void {
    this.accountService.confirmRecover(this.token, this.confirmRecoverForm.get('password').value).subscribe(response => {
      console.log(response);
      this.router.navigateByUrl('/auth/login');
      this.toastr.success('Successfully Recovered');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }
}
