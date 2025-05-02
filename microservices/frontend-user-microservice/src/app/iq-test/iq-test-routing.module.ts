import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from '../_guards/auth.guard';
import {IqTestComponent} from './iq-test.component';
import {IqTestRavenComponent} from './iq-test-raven/iq-test-raven.component';
import {IqAccessGuard} from '../_guards/iq.access.guard';
import {IqTestRavenResultsComponent} from './iq-test-raven-results/iq-test-raven-results.component';
import {AccessTestGuard} from '../_guards/access-test.guard';
import {LeaveTestGuard} from '../_guards/leave-test.guard';


const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: IqTestComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'raven-apm',
    pathMatch: 'full',
    component: IqTestRavenComponent,
    canActivate: [AuthGuard, IqAccessGuard, AccessTestGuard],
    canDeactivate: [LeaveTestGuard],
  },
  {
    path: 'raven-apm-results',
    pathMatch: 'full',
    component: IqTestRavenResultsComponent,
    canActivate: [AuthGuard]
  }
];


@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes),
  ]
})
export class IqTestRoutingModule { }
