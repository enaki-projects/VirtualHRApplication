import {Component, OnInit} from '@angular/core';
import {Publication} from '../../../_models/cv/references/publication';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {PublicationService} from '../../../_services/cv-references/publication.service';
import {ToastrService} from 'ngx-toastr';
import {BsModalRef} from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-cv-editor-publication-modal',
  templateUrl: './cv-editor-publication-modal.component.html',
  styleUrls: ['./cv-editor-publication-modal.component.css']
})
export class CvEditorPublicationModalComponent implements OnInit {

  publication: Publication = null;
  addAction = false;
  updateAction = false;

  publicationForm: FormGroup;
  today = new Date();
  private urlRegex = /(^$|https?:\/\/(www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*))/;

  constructor(
    private fb: FormBuilder,
    private publicationService: PublicationService,
    private toastr: ToastrService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorPublicationModalComponent] - constructor');
    this.publicationForm = this.fb.group({
      title: ['', [Validators.required]],
      year: ['', [Validators.required]],
      url: ['', [Validators.pattern(this.urlRegex)]],
      description: [''],
      reference: [''],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorPublicationModalComponent] - OnInit');
    this.initializeForm();
  }

  initializeForm(): void {
    if (this.updateAction){
      this.publicationForm = this.fb.group({
        title: [this.publication.title, [Validators.required]],
        year: [this.publication.year, [Validators.required]],
        url: [this.publication.url, [Validators.pattern(this.urlRegex)]],
        description: [this.publication.description],
        reference: [this.publication.reference],
      });
    } else if (this.addAction){
      this.publicationForm = this.fb.group({
        title: ['', [Validators.required]],
        year: ['', [Validators.required]],
        url: ['', [Validators.pattern(this.urlRegex)]],
        description: [''],
        reference: [''],
      });
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addPublication();
    } else if (this.updateAction){
      this.updatePublication();
    }
  }

  addPublication(): void{
    this.publicationService.addPublication(this.publicationForm.value).subscribe(publicationId => {
      this.publicationService.fetchUserPublication(publicationId).toPromise().then((publication) => {
        this.toastr.success('Publication Added Successfully');
        this.publicationForm.reset(this.publicationForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updatePublication(): void{
    this.publicationService.updatePublication(this.publication.publicationId, this.publicationForm.value).subscribe(() => {
      this.publicationService.fetchUserPublication(this.publication.publicationId).toPromise().then((publication) => {
        this.toastr.success('Publication Updated Successfully');
        this.publicationForm.reset(this.publicationForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }
}
