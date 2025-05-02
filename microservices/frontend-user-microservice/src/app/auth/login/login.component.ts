import {Component, OnInit} from '@angular/core';
import {faKey, faUser} from '@fortawesome/free-solid-svg-icons';
import {AccountService} from '../../_services/account.service';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { AvatarService } from 'src/app/_services/avatar.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  faUser = faUser;
  faKey = faKey;
  loginForm: FormGroup;

  constructor(
    public accountService: AccountService,
    public avatarService: AvatarService,
    private router: Router,
    private toastr: ToastrService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });
  }

  login(): void{
    this.accountService.login(this.loginForm.value).subscribe(response => {
      this.accountService.fetchUserCv().toPromise().then( () => {
          this.avatarService.setDefaultAvatarSettings();
          this.router.navigateByUrl('/home');
          this.toastr.success('Login Successfully');
      });
    }, error => {
      console.log(error);
      this.toastr.error('Invalid credentials');
    });
  }
}
