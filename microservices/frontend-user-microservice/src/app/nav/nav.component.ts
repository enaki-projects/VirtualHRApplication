import {Component, OnInit} from '@angular/core';
import {AccountService} from '../_services/account.service';
import {faBrain, faCogs, faEnvelopeOpenText, faHome, faSignOutAlt, faUserEdit} from '@fortawesome/free-solid-svg-icons';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {IqService} from '../_services/iq/iq.service';

@Component({
  standalone: false,
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {
  faCogs = faCogs;
  faHome = faHome;
  faUserEdit = faUserEdit;
  faSignOutAlt = faSignOutAlt;
  faEnvelopeOpenText = faEnvelopeOpenText;
  faBrain = faBrain;
  hasStartedTheTest = false;

  constructor(public accountService: AccountService,
              private iqService: IqService,
              private router: Router,
              private toastr: ToastrService) {
    iqService.hasStartedTheTest$.subscribe((hasStartedTheTest) => {
      this.hasStartedTheTest = hasStartedTheTest;
    });
  }

  ngOnInit(): void {
  }

  logout(): void {
    this.accountService.logout();
    this.toastr.success('Logged out Successfully.');
    this.router.navigateByUrl('/auth/login');
  }

}
