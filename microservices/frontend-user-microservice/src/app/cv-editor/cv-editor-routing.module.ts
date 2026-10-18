import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CvEditorComponent} from './cv-editor.component';
import {CvEditorGeneralInfoComponent} from './cv-editor-general-info/cv-editor-general-info.component';
import {AuthGuard} from '../_guards/auth.guard';
import {TypeaheadModule} from 'ngx-bootstrap/typeahead';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: CvEditorComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'general-info',
    pathMatch: 'full',
    component: CvEditorGeneralInfoComponent,
    canActivate: [AuthGuard]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes),
    TypeaheadModule
  ],
  exports: [RouterModule]
})
export class CvEditorRoutingModule { }
