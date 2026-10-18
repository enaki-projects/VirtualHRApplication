import {Component, OnInit} from '@angular/core';
import {ProfessionalExperience} from '../../../_models/cv/references/professional-experience';
import {faFlag} from '@fortawesome/free-solid-svg-icons';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {ProfessionalExperienceService} from '../../../_services/cv-references/professional-experience.service';
import {ToastrService} from 'ngx-toastr';
import {Organisation} from '../../../_models/cv/general/organisation';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Country} from '../../../_models/cv/general/country';
import {Observable, of, Subscriber} from 'rxjs';
import {InstitutionService} from '../../../_services/institution.service';
import {GeographyService} from '../../../_services/geography.service';
import {mergeMap} from 'rxjs/operators';
import {City} from '../../../_models/cv/general/city';
import {Region} from '../../../_models/cv/general/region';

@Component({
  standalone: false,
  selector: 'app-cv-editor-professional-experience-modal',
  templateUrl: './cv-editor-professional-experience-modal.component.html',
  styleUrls: ['./cv-editor-professional-experience-modal.component.css']
})
export class CvEditorProfessionalExperienceModalComponent implements OnInit {
  professionalExperience: ProfessionalExperience = null;
  addAction = false;
  updateAction = false;

  professionalExperienceForm: FormGroup;

  toPresent = false;
  today = new Date();
  organisations: Organisation[] = [];

  cities: City[] = [];
  regions: Region[] = [];
  countries: Country[] = [];

  selectedCity: City;
  selectedRegion: Region;
  selectedCountry: Country;
  faFlag = faFlag;

  protected searchStr: string;

  dataSource: Observable<Organisation[]>;
  typeaheadLoading: boolean;

  constructor(
    private fb: FormBuilder,
    private professionalExperienceService: ProfessionalExperienceService,
    private toastr: ToastrService,
    public activeModal: BsModalRef,
    private institutionService: InstitutionService,
    private geographyService: GeographyService,
  ) {
    console.log('[CvEditorProfessionalExperienceModalComponent] - constructor');
    this.professionalExperienceForm = this.fb.group({
      cityId: ['', [Validators.required]],
      organisationName: ['', [Validators.required]],
      jobName: ['', [Validators.required]],
      description: [''],
      fromDate: ['', [Validators.required]],
      toDate: [''],
    });

    this.dataSource = new Observable((observer: Subscriber<string>) => {
      observer.next(this.professionalExperienceForm.value.organisationName || '');
    })
      .pipe(
        mergeMap((token: string) => this.getStatesAsObservable(token))
      );
  }

  ngOnInit(): void {
    console.log('[CvEditorModal] - OnInit');
    this.geographyService.getCountriesWhichHaveCities().then((countries => {
      this.countries = countries;
      this.initializeForm();
    }));
  }

  initializeForm(): void {
    if (this.updateAction){
      this.toPresent = !this.professionalExperience.toDate;
      this.selectedCountry = this.professionalExperience.organisation.city.region.country;
      this.selectedRegion = this.professionalExperience.organisation.city.region;
      this.selectedCity = this.professionalExperience.organisation.city;


      this.professionalExperienceForm = this.fb.group({
        cityId: [this.professionalExperience.organisation.city.cityId, [Validators.required]],
        organisationName: [this.professionalExperience.organisation.organisationName, [Validators.required]],
        jobName: [this.professionalExperience.jobName, [Validators.required]],
        description: [this.professionalExperience.description],
        fromDate: [new Date(this.professionalExperience.fromDate), [Validators.required]],
        toDate: [(this.professionalExperience.toDate) ? new Date(this.professionalExperience.toDate) : ''],
      });
      this.onCountryChange(this.selectedCountry.countryId, true);

    } else if (this.addAction){
      this.professionalExperienceForm = this.fb.group({
        cityId: ['', [Validators.required]],
        organisationName: ['', [Validators.required]],
        jobName: ['', [Validators.required]],
        description: [''],
        fromDate: ['', [Validators.required]],
        toDate: [''],
      });
      this.onCountryChange(this.countries[0].countryId);
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addProfessionalExperience();
    } else if (this.updateAction){
      this.updateProfessionalExperience();
    }
  }

  addProfessionalExperience(): void{
    this.professionalExperienceService.addProfessionalExperience(this.professionalExperienceForm.value).subscribe(professionalExperienceId => {
      this.professionalExperienceService.fetchUserProfessionalExperience(professionalExperienceId).toPromise().then((professionalExperience) => {
        this.toastr.success('Professional Experience Added');
        this.organisations.push(this.professionalExperienceForm.value.organisationName);
        this.professionalExperienceForm.reset(this.professionalExperienceForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updateProfessionalExperience(): void{
    this.professionalExperienceService.updateProfessionalExperience(this.professionalExperience.professionalExperienceId, this.professionalExperienceForm.value).subscribe(() => {
      this.professionalExperienceService.fetchUserProfessionalExperience(this.professionalExperience.professionalExperienceId).toPromise().then((professionalExperience) => {
        this.toastr.success('Professional Experience Updated');
        this.organisations.push(this.professionalExperienceForm.value.organisationName);
        this.professionalExperienceForm.reset(this.professionalExperienceForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  toDateMaxValue(): Date{
    if (this.professionalExperienceForm.value.toDate && this.professionalExperienceForm.value.toDate < this.today){
      return this.professionalExperienceForm.value.toDate;
    }
    return this.today;
  }

  onToPresentChange(event): void {
    this.toPresent = event.target.checked;
    if (this.toPresent){
      this.professionalExperienceForm.controls.toDate.setValue(null);
    } else {
      if (this.professionalExperienceForm.value.fromDate && this.professionalExperienceForm.value.fromDate > this.today){
        this.professionalExperienceForm.controls.toDate.setValue(this.professionalExperienceForm.value.fromDate);
      } else {
        this.professionalExperienceForm.controls.toDate.setValue(this.today);
      }
    }
  }

  onChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCountry = this.countries.find(country => country.countryId == id);
  }

  onCountryChange(id: number, keepSelected= false): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCountry = this.countries.find(country => country.countryId == id);
    this.geographyService.getRegionsByCountryIdWhichHaveCities(this.selectedCountry.countryId).then((regions => {
      this.regions = regions;
      if (regions?.length > 0){
        this.onRegionChange(regions[0].regionId, keepSelected);
      }
      // this.selectedCity = null;
    }));
  }

  onRegionChange(id: number, keepSelected= false): void {
    if (!keepSelected){
      // tslint:disable-next-line:triple-equals
      this.selectedRegion = this.regions.find(region => region.regionId == id);
    }
    this.geographyService.getCitiesByRegionId(this.selectedRegion.regionId).then((cities => {
      this.cities = cities;
      if (cities?.length > 0){
        this.onCityChange(cities[0].cityId, keepSelected);
      }
    }));
  }

  onCityChange(id: number, keepSelected= false): void {
    if (!keepSelected) {
      // tslint:disable-next-line:triple-equals
      this.selectedCity = this.cities.find(city => city.cityId == id);
      this.professionalExperienceForm.patchValue({cityId: this.selectedCity.cityId});
    }
    this.institutionService.getBasicProfessionalInstitutionsByCityId(this.selectedCity.cityId).then((companies => {
      if (companies !== null){
        this.organisations = companies;
      }
    }));
  }

  getStatesAsObservable(token: string): Observable<Organisation[]> {
    const query = new RegExp(token, 'i');
    return of(
      this.organisations.filter((state: Organisation) => {
        // console.log(state);
        return query.test(state.organisationName);
      })
    );
  }

  changeTypeaheadLoading(e: boolean): void {
    this.typeaheadLoading = e;
  }

  typeaheadOnSelect(event): void {
    const company = this.organisations.find(organisation => organisation.organisationName === event.value);
    this.professionalExperienceForm.controls.organisationName.setValue(company.organisationName);
  }
}
