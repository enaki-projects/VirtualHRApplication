import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HomeComponent} from './home/home.component';
import {LoginComponent} from './auth/login/login.component';
import {RegisterComponent} from './auth/register/register.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppRoutingModule} from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {JwtInterceptor} from './_interceptors/jwt.interceptor';
import {NgxSpinnerModule} from 'ngx-spinner';
import {LoadingInterceptor} from './_interceptors/loading.interceptor';
import {CvViewComponent} from './cv-view/cv-view.component';
import {CvViewDetailsComponent} from './cv-view/cv-view-details/cv-view-details.component';
import {SharedModule} from './shared/shared.module';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {NavComponent} from './nav/nav.component';
import {CountdownModule} from 'ngx-countdown';
import { SettingsPageComponent } from './settings-page/settings-page.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    CvViewComponent,
    CvViewDetailsComponent,
    NavComponent,
    SettingsPageComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    SharedModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgxSpinnerModule,
    FontAwesomeModule,
    CountdownModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: LoadingInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
