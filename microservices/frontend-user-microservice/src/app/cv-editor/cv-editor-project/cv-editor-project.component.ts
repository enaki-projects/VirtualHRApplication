import {Component, Input, OnInit} from '@angular/core';
import {Project} from '../../_models/cv/references/project';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {ProjectService} from '../../_services/cv-references/project.service';
import {ToastrService} from 'ngx-toastr';
import {CvEditorProjectModalComponent} from '../_modals/cv-editor-project-modal/cv-editor-project-modal.component';
import {faEye, faEyeSlash, faPen, faTrash} from '@fortawesome/free-solid-svg-icons';

@Component({
  standalone: false,
  selector: 'app-cv-editor-project',
  templateUrl: './cv-editor-project.component.html',
  styleUrls: ['./cv-editor-project.component.css']
})
export class CvEditorProjectComponent implements OnInit {

  @Input() projects: Project[];
  faPen = faPen;
  faTrash = faTrash;
  faEye = faEye;
  faEyeSlash = faEyeSlash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private projectService: ProjectService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteProject(projectId: number): void {
    this.projectService.deleteProject(projectId).toPromise().then(result => {
      this.projectService.fetchDeletedProject(projectId);
      this.toastr.info('Project Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(project: Project): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        project,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorProjectModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorProjectModalComponent, config);
  }

}
