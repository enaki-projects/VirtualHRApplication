import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {City} from '../../../_models/cv/general/city';
import {Country} from '../../../_models/cv/general/country';
import {LocationService} from '../../../_services/cv-references/location.service';
import {ToastrService} from 'ngx-toastr';
import {GeographyService} from '../../../_services/geography.service';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {UserLocation} from '../../../_models/cv/references/userLocation';
import {faFlag} from '@fortawesome/free-solid-svg-icons';
import {Region} from '../../../_models/cv/general/region';

@Component({
  standalone: false,
  selector: 'app-cv-editor-location-modal',
  templateUrl: './cv-editor-location-modal.component.html',
  styleUrls: ['./cv-editor-location-modal.component.css']
})
export class CvEditorLocationModalComponent implements OnInit {
  userLocation: UserLocation = null;
  addAction = false;
  updateAction = false;
  faFlag = faFlag;

  locationForm: FormGroup;

  cities: City[] = [];
  regions: Region[] = [];
  countries: Country[] = [];

  selectedCity: City;
  selectedRegion: Region;
  selectedCountry: Country;

  locationTypes = ['Home', 'Work', 'Other'];

  constructor(
    private fb: FormBuilder,
    private locationService: LocationService,
    private toastr: ToastrService,
    private geographyService: GeographyService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorLocationModalComponent] - constructor');
    this.locationForm = this.fb.group({
      cityId: [{value: ''}, [Validators.required]],
      type: ['', [Validators.required]],
      address: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorLocationModalComponent] - OnInit');
    this.geographyService.getCountriesWhichHaveCities().then((countries => {
      this.countries = countries;
      this.initializeForm();
    }));
  }

  initializeForm(): void {
    if (this.updateAction) {
      this.locationForm = this.fb.group({
        cityId: [this.userLocation.city.cityId, [Validators.required]],
        type: [this.userLocation.type, [Validators.required]],
        address: [this.userLocation.address, [Validators.required]],
      });
      this.selectedCity = this.userLocation.city;
      this.selectedRegion = this.userLocation.city.region;
      this.selectedCountry = this.userLocation.city.region.country;
      this.geographyService.getRegionsByCountryIdWhichHaveCities(this.selectedCountry.countryId).then((regions => {
        this.regions = regions;
        this.geographyService.getCitiesByRegionId(this.selectedRegion.regionId).then((cities => {
          this.cities = cities;
        }));
      }), error => {
        console.log(error);
      });
    } else if (this.addAction) {
      this.locationForm = this.fb.group({
        cityId: ['', [Validators.required]],
        type: ['Home', [Validators.required]],
        address: ['', [Validators.required]],
      });
      this.onCountryChange(this.countries[0].countryId);
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addLocation();
    } else if (this.updateAction){
      this.updateLocation();
    }
  }


  addLocation(): void{
    this.locationService.addLocation(this.locationForm.value).subscribe(userLocationId => {
      this.locationService.fetchUserLocation(userLocationId).toPromise().then((location) => {
        this.toastr.success('Location Added Successfully');
        this.locationForm.reset(this.locationForm.value);
        this.activeModal.hide();
      });
    });
  }

  updateLocation(): void{
    this.locationService.updateLocation(this.userLocation.locationId, this.locationForm.value).subscribe(() => {
      this.locationService.fetchUserLocation(this.userLocation.locationId).toPromise().then((location) => {
        this.toastr.success('Location Updated Successfully');
        this.locationForm.reset(this.locationForm.value);
        this.activeModal.hide();
      });
    });
  }


  onCountryChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCountry = this.countries.find(country => country.countryId == id);
    this.geographyService.getRegionsByCountryIdWhichHaveCities(this.selectedCountry.countryId).then((regions => {
      this.regions = regions;
      if (regions?.length > 0){
        this.onRegionChange(regions[0].regionId);
      }
      // this.selectedCity = null;
    }));
  }

  onRegionChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedRegion = this.regions.find(region => region.regionId == id);
    this.geographyService.getCitiesByRegionId(this.selectedRegion.regionId).then((cities => {
      this.cities = cities;
      this.selectedCity = cities[0];
      this.locationForm.patchValue({cityId: this.selectedCity.cityId});
    }));
  }

  onCityChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCity = this.cities.find(city => city.cityId == id);
    this.locationForm.patchValue({cityId: this.selectedCity.cityId});
  }

}
