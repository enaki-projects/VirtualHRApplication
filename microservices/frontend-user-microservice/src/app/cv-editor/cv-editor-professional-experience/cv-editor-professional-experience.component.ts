import {Component, Input, OnInit} from '@angular/core';
import {ProfessionalExperience} from '../../_models/cv/references/professional-experience';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {ProfessionalExperienceService} from '../../_services/cv-references/professional-experience.service';
import {ToastrService} from 'ngx-toastr';
import {faEye, faEyeSlash, faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {CvEditorProfessionalExperienceModalComponent} from '../_modals/cv-editor-professional-experience-modal/cv-editor-professional-experience-modal.component';

@Component({
  standalone: false,
  selector: 'app-cv-editor-professional-experience',
  templateUrl: './cv-editor-professional-experience.component.html',
  styleUrls: ['./cv-editor-professional-experience.component.css']
})
export class CvEditorProfessionalExperienceComponent implements OnInit {

  @Input() professionalExperiences: ProfessionalExperience[];
  faPen = faPen;
  faTrash = faTrash;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private professionalExperienceService: ProfessionalExperienceService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    console.log(this.professionalExperiences);
  }

  deleteProfessionalExperience(professionalExperienceId: number): void {
    this.professionalExperienceService.deleteProfessionalExperience(professionalExperienceId).toPromise().then(result => {
      this.professionalExperienceService.fetchDeletedProfessionalExperience(professionalExperienceId);
      this.toastr.info('ProfessionalExperience Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(professionalExperience: ProfessionalExperience): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        professionalExperience,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorProfessionalExperienceModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorProfessionalExperienceModalComponent, config);
  }
}
