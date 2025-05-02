import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {GeographyService} from '../../../_services/geography.service';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {CitizenshipService} from '../../../_services/cv-references/citizenship.service';
import {Citizenship} from '../../../_models/cv/references/citizenship';
import {faFlag} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-cv-editor-nationality-modal',
  templateUrl: './cv-editor-nationality-modal.component.html',
  styleUrls: ['./cv-editor-nationality-modal.component.css']
})
export class CvEditorNationalityModalComponent implements OnInit {
  nationalities: Citizenship[];
  citizenshipForm: FormGroup;
  selectedNationality: Citizenship = null;
  faFlag = faFlag;

  constructor(
    private fb: FormBuilder,
    private citizenshipService: CitizenshipService,
    private toastr: ToastrService,
    private geographyService: GeographyService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorNationalityModalComponent] - constructor');
    this.citizenshipForm = this.fb.group({
      nationalityId: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorModal] - OnInit');
    this.geographyService.getNationalities().then(nationalities => {
      this.nationalities = nationalities;
      this.initializeForm();
    });

  }

  initializeForm(): void {
    this.selectedNationality = this.nationalities.find(nationality => nationality.nationalityName === 'American');
    this.citizenshipForm = this.fb.group({
      nationalityId: [this.selectedNationality.nationalityId, [Validators.required]],
    });
  }

  addCitizenship(): void{
    this.citizenshipService.addCitizenship(this.citizenshipForm.value.nationalityId).subscribe(() => {
      this.citizenshipService.fetchUserCitizenship(this.citizenshipForm.value.nationalityId).toPromise().then(() => {
        this.toastr.success('Citizenship Added Successfully');
        this.citizenshipForm.reset(this.citizenshipForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  onChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedNationality = this.nationalities.find(nationality => nationality.nationalityId == id);
  }
}
