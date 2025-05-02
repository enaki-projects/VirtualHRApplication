import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {EducationalExperience} from '../../../_models/cv/references/educational-experience';
import {EducationalExperienceService} from '../../../_services/cv-references/educational-experience.service';
import {InstitutionService} from '../../../_services/institution.service';
import {EducationalInstitution} from '../../../_models/cv/general/educational-institution';
import {Country} from '../../../_models/cv/general/country';
import {GeographyService} from '../../../_services/geography.service';
import {faFlag} from '@fortawesome/free-solid-svg-icons';
import {Observable, of, Subscriber} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

@Component({
  selector: 'app-cv-editor-educational-experience-modal',
  templateUrl: './cv-editor-educational-experience-modal.component.html',
  styleUrls: ['./cv-editor-educational-experience-modal.component.css']
})
export class CvEditorEducationalExperienceModalComponent implements OnInit {

  educationalExperience: EducationalExperience = null;
  addAction = false;
  updateAction = false;

  educationalExperienceForm: FormGroup;

  toPresent = false;
  today = new Date();
  educationalInstitutions: EducationalInstitution[];

  selectedCountry: Country = null;
  countries: Country[];
  faFlag = faFlag;

  protected searchStr: string;

  dataSource: Observable<EducationalInstitution[]>;
  typeaheadLoading: boolean;

  constructor(
    private fb: FormBuilder,
    private educationalExperienceService: EducationalExperienceService,
    private toastr: ToastrService,
    public activeModal: BsModalRef,
    private institutionService: InstitutionService,
    private geographyService: GeographyService,
  ) {
    console.log('[CvEditorEducationalExperienceModalComponent] - constructor');
    this.educationalExperienceForm = this.fb.group({
      educationalInstitutionId: ['', [Validators.required]],
      institutionName: ['', [Validators.required]],
      specialty: ['', [Validators.required]],
      qualificationTitle: ['', [Validators.required]],
      fromDate: ['', [Validators.required]],
      toDate: [''],
    });

    this.dataSource = new Observable((observer: Subscriber<string>) => {
      observer.next(this.educationalExperienceForm.value.institutionName || '');
    })
      .pipe(
        mergeMap((token: string) => this.getStatesAsObservable(token))
      );
  }

  ngOnInit(): void {
    console.log('[CvEditorModal] - OnInit');
    this.geographyService.getCountriesWhichHaveUniversities().then((countries => {
      this.countries = countries;
      this.initializeForm();
    }));
  }

  initializeForm(): void {
    if (this.updateAction){
      this.toPresent = !this.educationalExperience.toDate;
      this.selectedCountry = this.educationalExperience.educationalInstitution.country;
      this.onCountryChange(this.selectedCountry.countryId, false);

      this.educationalExperienceForm = this.fb.group({
        educationalInstitutionId: [this.educationalExperience.educationalInstitution.educationalInstitutionId, [Validators.required]],
        institutionName: [this.educationalExperience.educationalInstitution.institutionName, [Validators.required]],
        specialty: [this.educationalExperience.specialty, [Validators.required]],
        qualificationTitle: [this.educationalExperience.qualificationTitle, [Validators.required]],
        fromDate: [new Date(this.educationalExperience.fromDate), [Validators.required]],
        toDate: [(this.educationalExperience.toDate) ? new Date(this.educationalExperience.toDate) : ''],
      });
    } else if (this.addAction){
      this.educationalExperienceForm = this.fb.group({
        educationalInstitutionId: ['', [Validators.required]],
        institutionName: ['', [Validators.required]],
        specialty: ['', [Validators.required]],
        qualificationTitle: ['', [Validators.required]],
        fromDate: ['', [Validators.required]],
        toDate: [''],
      });
      this.onCountryChange(this.countries[0].countryId);
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addEducationalExperience();
    } else if (this.updateAction){
      this.updateEducationalExperience();
    }
  }

  addEducationalExperience(): void{
    const eduInstitution = this.educationalInstitutions.find(educationalInstitution => educationalInstitution.institutionName === this.educationalExperienceForm.value.institutionName);
    if (!eduInstitution){
      this.toastr.error('University Not Found');
      return;
    }
    this.educationalExperienceService.addEducationalExperience(this.educationalExperienceForm.value).subscribe(educationalExperienceId => {
      this.educationalExperienceService.fetchUserEducationalExperience(educationalExperienceId).toPromise().then((educationalExperience) => {
        this.toastr.success('Educational Experience Added');
        this.educationalExperienceForm.reset(this.educationalExperienceForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updateEducationalExperience(): void{
    const eduInstitution = this.educationalInstitutions.find(educationalInstitution => educationalInstitution.institutionName === this.educationalExperienceForm.value.institutionName);
    if (!eduInstitution){
      this.toastr.error('University Not Found');
      return;
    }
    this.educationalExperienceService.updateEducationalExperience(this.educationalExperience.educationalExperienceId, this.educationalExperienceForm.value).subscribe(() => {
      this.educationalExperienceService.fetchUserEducationalExperience(this.educationalExperience.educationalExperienceId).toPromise().then((educationalExperience) => {
        this.toastr.success('Educational Experience Updated');
        this.educationalExperienceForm.reset(this.educationalExperienceForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  toDateMaxValue(): Date{
    if (this.educationalExperienceForm.value.toDate && this.educationalExperienceForm.value.toDate < this.today){
      return this.educationalExperienceForm.value.toDate;
    }
    return this.today;
  }

  onToPresentChange(event): void {
    this.toPresent = event.target.checked;
    if (this.toPresent){
      this.educationalExperienceForm.controls.toDate.setValue(null);
    } else {
      if (this.educationalExperienceForm.value.fromDate && this.educationalExperienceForm.value.fromDate > this.today){
        this.educationalExperienceForm.controls.toDate.setValue(this.educationalExperienceForm.value.fromDate);
      } else {
        this.educationalExperienceForm.controls.toDate.setValue(this.today);
      }
    }
  }

  onChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCountry = this.countries.find(country => country.countryId == id);
  }

  onCountryChange(id: number, setInstitutionName = true): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCountry = this.countries.find(country => country.countryId == id);
    this.institutionService.getBasicEducationalInstitutionsByCountryId(this.selectedCountry.countryId).then((educationalInstitutions => {
      this.educationalInstitutions = educationalInstitutions;
      if (setInstitutionName){
        this.educationalExperienceForm.controls.institutionName.setValue('');
      }
    }));
  }

  getStatesAsObservable(token: string): Observable<EducationalInstitution[]> {
    const query = new RegExp(token, 'i');
    return of(
      this.educationalInstitutions.filter((state: EducationalInstitution) => {
        // console.log(state);
        return query.test(state.institutionName);
      })
    );
  }

  changeTypeaheadLoading(e: boolean): void {
    this.typeaheadLoading = e;
  }

  typeaheadOnSelect(event): void {
    const eduInstitution = this.educationalInstitutions.find(educationalInstitution => educationalInstitution.institutionName === event.value);
    this.educationalExperienceForm.controls.educationalInstitutionId.setValue(eduInstitution.educationalInstitutionId);
  }
}
