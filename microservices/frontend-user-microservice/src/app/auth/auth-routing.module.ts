import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './login/login.component';
import {LoginAuthGuard} from '../_guards/login.auth.guard';
import {RegisterComponent} from './register/register.component';
import {ForgetPasswordComponent} from './forget-password/forget-password.component';

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginAuthGuard]
  },
  {
    path: 'register',
    component: RegisterComponent,
    canActivate: [LoginAuthGuard]
  },
  {
    path: 'recover',
    component: ForgetPasswordComponent,
    canActivate: [LoginAuthGuard]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
