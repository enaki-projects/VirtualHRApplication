import {Component, Input, OnInit} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {faBriefcase, faBuilding, faHome, faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {ToastrService} from 'ngx-toastr';
import {UserLocation} from '../../_models/cv/references/userLocation';
import {CvEditorLocationModalComponent} from '../_modals/cv-editor-location-modal/cv-editor-location-modal.component';
import {LocationService} from '../../_services/cv-references/location.service';

@Component({
  selector: 'app-cv-editor-location',
  templateUrl: './cv-editor-location.component.html',
  styleUrls: ['./cv-editor-location.component.css']
})
export class CvEditorLocationComponent implements OnInit {
  @Input() locations: UserLocation[];
  faHome = faHome;
  faBuilding = faBuilding;
  faBriefcase = faBriefcase;
  faPen = faPen;
  faTrash = faTrash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private locationService: LocationService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteLocation(locationId: number): void {
    this.locationService.deleteLocation(locationId).toPromise().then(result => {
      this.locationService.fetchDeletedLocation(locationId);
      this.toastr.info('Location Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(location: UserLocation): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        userLocation: location,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorLocationModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorLocationModalComponent, config);
  }

}
