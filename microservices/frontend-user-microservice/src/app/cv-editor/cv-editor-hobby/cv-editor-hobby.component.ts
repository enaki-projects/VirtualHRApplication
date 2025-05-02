import {Component, Input, OnInit} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {ToastrService} from 'ngx-toastr';
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {Hobby} from '../../_models/cv/references/hobby';
import {HobbyService} from '../../_services/cv-references/hobby.service';
import {CvEditorHobbyModalComponent} from '../_modals/cv-editor-hobby-modal/cv-editor-hobby-modal.component';

@Component({
  selector: 'app-cv-editor-hobby',
  templateUrl: './cv-editor-hobby.component.html',
  styleUrls: ['./cv-editor-hobby.component.css']
})
export class CvEditorHobbyComponent implements OnInit {

  @Input() hobbies: Hobby[];
  faPen = faPen;
  faTrash = faTrash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private hobbyService: HobbyService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteHobby(hobbyId: number): void {
    this.hobbyService.deleteHobby(hobbyId).toPromise().then(result => {
      this.hobbyService.fetchDeletedHobby(hobbyId);
      this.toastr.info('Hobby Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(hobby: Hobby): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        hobby,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorHobbyModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorHobbyModalComponent, config);
  }

}
