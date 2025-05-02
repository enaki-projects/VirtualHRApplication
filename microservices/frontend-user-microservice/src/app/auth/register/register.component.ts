import {Component, OnInit} from '@angular/core';
import {AccountService} from '../../_services/account.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {faFileSignature, faKey, faTable, faUser, faVenusMars} from '@fortawesome/free-solid-svg-icons';
import {AbstractControl, FormBuilder, FormGroup, ValidatorFn, Validators} from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  faUser = faUser;
  faKey = faKey;
  faFileSignature = faFileSignature;
  faVenusMars = faVenusMars;
  faTable = faTable;
  model: any = {};
  registerForm: FormGroup;
  maxDate: Date;


  constructor(
    public accountService: AccountService,
    private router: Router,
    private toastr: ToastrService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.initializeForm();
    this.maxDate = new Date();
    this.maxDate.setFullYear(this.maxDate.getFullYear() - 18);
  }

  validateEmail(email): boolean {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
  }

  register(): void{
    this.accountService.register(this.registerForm.value).subscribe(response => {
      console.log(response);
      this.router.navigateByUrl('/auth/login');
      this.toastr.success('Register Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  initializeForm(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      passwordConfirm: ['',  [Validators.required, this.matchValues('password')]],
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      gender: ['', [Validators.required]],
      birthdate: ['', [Validators.required]],

    });
  }

  matchValues(matchTo: string): ValidatorFn {
    return (control: AbstractControl) => {
      return control?.value === control?.parent?.controls[matchTo].value
        ? null : {isMatching: true};
    };
  }
}
