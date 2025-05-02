import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {faFlag, faPhoneAlt} from '@fortawesome/free-solid-svg-icons';
import {ProjectService} from '../../../_services/cv-references/project.service';
import {Project} from '../../../_models/cv/references/project';

@Component({
  selector: 'app-cv-editor-project-modal',
  templateUrl: './cv-editor-project-modal.component.html',
  styleUrls: ['./cv-editor-project-modal.component.css']
})
export class CvEditorProjectModalComponent implements OnInit {

  project: Project = null;
  addAction = false;
  updateAction = false;

  projectForm: FormGroup;
  faPhoneAlt = faPhoneAlt;
  faFlag = faFlag;

  toPresent = false;
  today = new Date();

  private urlRegex = /(^$|https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*))/;

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private toastr: ToastrService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorProjectModalComponent] - constructor');
    this.projectForm = this.fb.group({
      url: ['', [Validators.pattern(this.urlRegex)]],
      name: ['', [Validators.required]],
      description: [''],
      fromDate: ['', [Validators.required]],
      toDate: [''],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorModal] - OnInit');
    this.initializeForm();
    console.log(this.project);
  }

  initializeForm(): void {
    if (this.updateAction){
      this.toPresent = !this.project.toDate;
      this.projectForm = this.fb.group({
        url: [this.project.url, [Validators.pattern(this.urlRegex)]],
        name: [this.project.name, [Validators.required]],
        description: [this.project.description],
        fromDate: [new Date(this.project.fromDate), [Validators.required]],
        toDate: [(this.project.toDate) ? new Date(this.project.toDate) : ''],
      });
    } else if (this.addAction){
      this.projectForm = this.fb.group({
        url: ['', [Validators.pattern(this.urlRegex)]],
        name: ['', [Validators.required]],
        description: [''],
        fromDate: ['', [Validators.required]],
        toDate: [''],
      });
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addProject();
    } else if (this.updateAction){
      this.updateProject();
    }
  }

  addProject(): void{
    this.projectService.addProject(this.projectForm.value).subscribe(projectId => {
      this.projectService.fetchUserProject(projectId).toPromise().then((project) => {
        this.toastr.success('Project Added Successfully');
        this.projectForm.reset(this.projectForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updateProject(): void{
    this.projectService.updateProject(this.project.projectId, this.projectForm.value).subscribe(() => {
      this.projectService.fetchUserProject(this.project.projectId).toPromise().then((project) => {
        this.toastr.success('Project Updated Successfully');
        this.projectForm.reset(this.projectForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  toDateMaxValue(): Date{
    if (this.projectForm.value.toDate && this.projectForm.value.toDate < this.today){
      return this.projectForm.value.toDate;
    }
    return this.today;
  }

  onToPresentChange(event): void {
    this.toPresent = event.target.checked;
    if (this.toPresent){
      this.projectForm.controls.toDate.setValue(null);
    } else {
      if (this.projectForm.value.fromDate && this.projectForm.value.fromDate > this.today){
        this.projectForm.controls.toDate.setValue(this.projectForm.value.fromDate);
      } else {
        this.projectForm.controls.toDate.setValue(this.today);
      }
    }
  }
}
