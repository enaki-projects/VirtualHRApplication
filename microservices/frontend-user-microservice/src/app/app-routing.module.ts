import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './home/home.component';
import {AuthGuard} from './_guards/auth.guard';
import {CvViewComponent} from './cv-view/cv-view.component';
import {SettingsPageComponent} from './settings-page/settings-page.component';


const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'home', component: HomeComponent, canActivate: [AuthGuard], data: { animation: 0}},
  {path: 'cv-view', component: CvViewComponent, canActivate: [AuthGuard], data: { animation: 1}},
  {
    path: 'cv-editor',
    data: { animation: 2},
    loadChildren: () =>
      import('./cv-editor/cv-editor.module').then((m) => m.CvEditorModule),
  },
  {
    path: 'iq-test',
    data: { animation: 3},
    loadChildren: () =>
      import('./iq-test/iq-test.module').then((m) => m.IqTestModule),
  },
  {path: 'settings', component: SettingsPageComponent, canActivate: [AuthGuard], data: { animation: 4}},
  {
    path: 'auth',
    data: { animation: 5},
    loadChildren: () =>
      import('./auth/auth.module').then((m) => m.AuthModule),
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
