import {Component, Input, OnInit} from '@angular/core';
import {Publication} from '../../_models/cv/references/publication';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {PublicationService} from '../../_services/cv-references/publication.service';
import {ToastrService} from 'ngx-toastr';
import {faEye, faEyeSlash, faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {CvEditorPublicationModalComponent} from '../_modals/cv-editor-publication-modal/cv-editor-publication-modal.component';

@Component({
  standalone: false,
  selector: 'app-cv-editor-publication',
  templateUrl: './cv-editor-publication.component.html',
  styleUrls: ['./cv-editor-publication.component.css']
})
export class CvEditorPublicationComponent implements OnInit {

  @Input() publications: Publication[];
  faPen = faPen;
  faTrash = faTrash;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private publicationService: PublicationService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deletePublication(publicationId: number): void {
    this.publicationService.deletePublication(publicationId).toPromise().then(result => {
      this.publicationService.fetchDeletedPublication(publicationId);
      this.toastr.info('Publication Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(publication: Publication): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        publication,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorPublicationModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorPublicationModalComponent, config);
  }

}
