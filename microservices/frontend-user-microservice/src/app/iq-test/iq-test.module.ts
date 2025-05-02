import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {IqTestRoutingModule} from './iq-test-routing.module';
import {IqTestRavenComponent} from './iq-test-raven/iq-test-raven.component';
import {IqTestComponent} from './iq-test.component';
import {FormsModule} from '@angular/forms';
import {SharedModule} from '../shared/shared.module';
import {RouterModule} from '@angular/router';
import {IqTestRavenResultsComponent} from './iq-test-raven-results/iq-test-raven-results.component';
import {CountdownModule} from 'ngx-countdown';


@NgModule({
  declarations: [
    IqTestRavenComponent,
    IqTestComponent,
    IqTestRavenResultsComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    SharedModule,
    IqTestRoutingModule,
    RouterModule,
    CountdownModule,
  ]
})
export class IqTestModule { }
