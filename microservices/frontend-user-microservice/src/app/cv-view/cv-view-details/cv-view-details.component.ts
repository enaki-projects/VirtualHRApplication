import {Component, Input, OnInit} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {CvService} from '../../_services/cv.service';
import {faBriefcase, faBuilding, faEnvelope, faGlobe, faHome, faPhoneAlt} from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-cv-view-details',
  templateUrl: './cv-view-details.component.html',
  styleUrls: ['./cv-view-details.component.css']
})
export class CvViewDetailsComponent implements OnInit {
  @Input() currentUser: AuthenticatedUser;

  faHome = faHome;
  faPhoneAlt = faPhoneAlt;
  faEnvelope = faEnvelope;
  faGlobe = faGlobe;
  faBriefcase = faBriefcase;
  faBuilding = faBuilding;

  constructor(
    public cvService: CvService
  ) { }

  ngOnInit(): void {
  }


}
