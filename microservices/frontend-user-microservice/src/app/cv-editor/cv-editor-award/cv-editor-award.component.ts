import {Component, Input, OnInit} from '@angular/core';
import {Award} from '../../_models/cv/references/award';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {ToastrService} from 'ngx-toastr';
import {faEye, faEyeSlash, faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {AwardService} from '../../_services/cv-references/award.service';
import {CvEditorAwardModalComponent} from '../_modals/cv-editor-award-modal/cv-editor-award-modal.component';

@Component({
  standalone: false,
  selector: 'app-cv-editor-award',
  templateUrl: './cv-editor-award.component.html',
  styleUrls: ['./cv-editor-award.component.css']
})
export class CvEditorAwardComponent implements OnInit {

  @Input() awards: Award[];
  faPen = faPen;
  faTrash = faTrash;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private awardService: AwardService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteAward(awardId: number): void {
    this.awardService.deleteAward(awardId).toPromise().then(result => {
      this.awardService.fetchDeletedAward(awardId);
      this.toastr.info('Award Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(award: Award): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        award,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorAwardModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorAwardModalComponent, config);
  }

}
