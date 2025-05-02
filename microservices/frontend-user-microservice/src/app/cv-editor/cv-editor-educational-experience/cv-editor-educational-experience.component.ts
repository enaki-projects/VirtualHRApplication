import {Component, Input, OnInit} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {ToastrService} from 'ngx-toastr';
import {faEye, faEyeSlash, faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {EducationalExperience} from '../../_models/cv/references/educational-experience';
import {EducationalExperienceService} from '../../_services/cv-references/educational-experience.service';
import {CvEditorEducationalExperienceModalComponent} from '../_modals/cv-editor-educational-experience-modal/cv-editor-educational-experience-modal.component';

@Component({
  selector: 'app-cv-editor-educational-experience',
  templateUrl: './cv-editor-educational-experience.component.html',
  styleUrls: ['./cv-editor-educational-experience.component.css']
})
export class CvEditorEducationalExperienceComponent implements OnInit {

  @Input() educationalExperiences: EducationalExperience[];
  faPen = faPen;
  faTrash = faTrash;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private educationalExperienceService: EducationalExperienceService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteEducationalExperience(educationalExperienceId: number): void {
    this.educationalExperienceService.deleteEducationalExperience(educationalExperienceId).toPromise().then(result => {
      this.educationalExperienceService.fetchDeletedEducationalExperience(educationalExperienceId);
      this.toastr.info('EducationalExperience Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(educationalExperience: EducationalExperience): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        educationalExperience,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorEducationalExperienceModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorEducationalExperienceModalComponent, config);
  }

}
