import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {faFlag} from '@fortawesome/free-solid-svg-icons';
import {HobbyService} from '../../../_services/cv-references/hobby.service';
import {Hobby} from '../../../_models/cv/references/hobby';

@Component({
  standalone: false,
  selector: 'app-cv-editor-hobby-modal',
  templateUrl: './cv-editor-hobby-modal.component.html',
  styleUrls: ['./cv-editor-hobby-modal.component.css']
})
export class CvEditorHobbyModalComponent implements OnInit {
  hobby: Hobby = null;
  addAction = false;
  updateAction = false;

  hobbies: Hobby[];
  hobbyForm: FormGroup;
  faFlag = faFlag;

  constructor(
    private fb: FormBuilder,
    private hobbyService: HobbyService,
    private toastr: ToastrService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorHobbyModalComponent] - constructor');
    this.hobbyForm = this.fb.group({
      name: ['', [Validators.required]],
      description: [''],
    });
  }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    if (this.updateAction){
      this.hobbyForm = this.fb.group({
        name: [this.hobby.name, [Validators.required]],
        description: [this.hobby.description],
      });
    } else if (this.addAction){
      this.hobbyForm = this.fb.group({
        name: ['', [Validators.required]],
        description: [''],
      });
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addHobby();
    } else if (this.updateAction){
      this.updateHobby();
    }
  }

  addHobby(): void{
    this.hobbyService.addHobby(this.hobbyForm.value).subscribe(hobbyId => {
      this.hobbyService.fetchUserHobby(hobbyId).toPromise().then((hobby) => {
        this.toastr.success('Hobby Added Successfully');
        this.hobbyForm.reset(this.hobbyForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updateHobby(): void{
    this.hobbyService.updateHobby(this.hobby.hobbyId, this.hobbyForm.value).subscribe(() => {
      this.hobbyService.fetchUserHobby(this.hobby.hobbyId).toPromise().then((hobby) => {
        this.toastr.success('Hobby Updated Successfully');
        this.hobbyForm.reset(this.hobbyForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

}
