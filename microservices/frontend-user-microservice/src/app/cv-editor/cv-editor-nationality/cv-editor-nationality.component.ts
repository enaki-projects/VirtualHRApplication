import {Component, Input, OnInit} from '@angular/core';
import {Citizenship} from '../../_models/cv/references/citizenship';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {ToastrService} from 'ngx-toastr';
import {CvEditorNationalityModalComponent} from '../_modals/cv-editor-nationality-modal/cv-editor-nationality-modal.component';
import {CitizenshipService} from '../../_services/cv-references/citizenship.service';
import {faTrash} from '@fortawesome/free-solid-svg-icons';

@Component({
  standalone: false,
  selector: 'app-cv-editor-nationality',
  templateUrl: './cv-editor-nationality.component.html',
  styleUrls: ['./cv-editor-nationality.component.css']
})
export class CvEditorNationalityComponent implements OnInit {
  @Input() nationalities: Citizenship[];

  bsModalRef: BsModalRef;
  faTrash = faTrash;

  constructor(private modalService: BsModalService,
              private citizenshipService: CitizenshipService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteCitizenship(nationalityId: number): void {
    this.citizenshipService.deleteCitizenship(nationalityId).toPromise().then(result => {
      this.citizenshipService.fetchDeletedCitizenship(nationalityId);
      this.toastr.info('Citizenship Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorNationalityModalComponent, config);
  }

}
