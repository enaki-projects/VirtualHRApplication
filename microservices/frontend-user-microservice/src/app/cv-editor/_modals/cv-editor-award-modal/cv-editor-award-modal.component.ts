import {Component, OnInit} from '@angular/core';
import {Award} from '../../../_models/cv/references/award';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AwardService} from '../../../_services/cv-references/award.service';
import {ToastrService} from 'ngx-toastr';
import {BsModalRef} from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-cv-editor-award-modal',
  templateUrl: './cv-editor-award-modal.component.html',
  styleUrls: ['./cv-editor-award-modal.component.css']
})
export class CvEditorAwardModalComponent implements OnInit {

  award: Award = null;
  addAction = false;
  updateAction = false;

  awardForm: FormGroup;
  today = new Date();

  constructor(
    private fb: FormBuilder,
    private awardService: AwardService,
    private toastr: ToastrService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorAwardModalComponent] - constructor');
    this.awardForm = this.fb.group({
      title: ['', [Validators.required]],
      dateOfAward: ['', [Validators.required]],
      awardingInstitution: ['', [Validators.required]],
      description: [''],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorAwardModalComponent] - OnInit');
    this.initializeForm();
  }

  initializeForm(): void {
    if (this.updateAction){
      this.awardForm = this.fb.group({
        title: [this.award.title, [Validators.required]],
        dateOfAward: [this.award.dateOfAward, [Validators.required]],
        awardingInstitution: [this.award.awardingInstitution, [Validators.required]],
        description: [this.award.description],
      });
    } else if (this.addAction){
      this.awardForm = this.fb.group({
        title: ['', [Validators.required]],
        dateOfAward: ['', [Validators.required]],
        awardingInstitution: ['', [Validators.required]],
        description: [''],
      });
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addAward();
    } else if (this.updateAction){
      this.updateAward();
    }
  }

  addAward(): void{
    this.awardService.addAward(this.awardForm.value).subscribe(awardId => {
      this.awardService.fetchUserAward(awardId).toPromise().then((award) => {
        this.toastr.success('Award Added Successfully');
        this.awardForm.reset(this.awardForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updateAward(): void{
    this.awardService.updateAward(this.award.awardId, this.awardForm.value).subscribe(() => {
      this.awardService.fetchUserAward(this.award.awardId).toPromise().then((award) => {
        this.toastr.success('Award Updated Successfully');
        this.awardForm.reset(this.awardForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

}
