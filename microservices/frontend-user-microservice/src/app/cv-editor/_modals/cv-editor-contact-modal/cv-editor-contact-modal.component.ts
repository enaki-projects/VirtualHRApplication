import {Component, OnInit} from '@angular/core';
import {PhoneNumber} from '../../../_models/cv/references/phone-number';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {Country} from '../../../_models/cv/general/country';
import {GeographyService} from '../../../_services/geography.service';
import {faFlag, faPhoneAlt} from '@fortawesome/free-solid-svg-icons';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {ContactsService} from '../../../_services/cv-references/contacts.service';

@Component({
  standalone: false,
  selector: 'app-cv-editor-contact-modal',
  templateUrl: './cv-editor-contact-modal.component.html',
  styleUrls: ['./cv-editor-contact-modal.component.css']
})
export class CvEditorContactModalComponent implements OnInit {
  phoneNumber: PhoneNumber = null;
  addAction = false;
  updateAction = false;

  selectedCountry: Country = null;
  contactForm: FormGroup;
  countries: Country[];
  faPhoneAlt = faPhoneAlt;
  faFlag = faFlag;

  constructor(
    private fb: FormBuilder,
    private contactsService: ContactsService,
    private toastr: ToastrService,
    private geographyService: GeographyService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorContactModalComponent] - constructor');
    this.contactForm = this.fb.group({
      countryId: ['', [Validators.required]],
      number: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorModal] - OnInit');
    this.geographyService.getCountries().then((countries => {
      this.countries = countries;
      this.initializeForm();
    }));
  }

  initializeForm(): void {
    if (this.updateAction){
      this.contactForm = this.fb.group({
        countryId: [this.phoneNumber.country.countryId, [Validators.required]],
        number: [this.phoneNumber.number.replace(/ /g, ''), [Validators.required]],
      });
      this.selectedCountry = this.phoneNumber.country;
    } else if (this.addAction){
      this.selectedCountry = this.countries.find(country => country.iso2 === 'US');
      this.contactForm = this.fb.group({
        countryId: [this.selectedCountry.countryId, [Validators.required]],
        number: ['', [Validators.required]],
      });
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addPhoneNumber();
    } else if (this.updateAction){
      this.updatePhoneNumber();
    }
  }

  addPhoneNumber(): void{
    this.contactsService.addPhoneNumber(this.contactForm.value).subscribe(phoneId => {
      this.contactsService.fetchUserPhoneNumber(phoneId).toPromise().then((phoneNumber) => {
        this.toastr.success('Phone Number Added Successfully');
        this.contactForm.reset(this.contactForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updatePhoneNumber(): void{
    this.contactsService.updatePhoneNumber(this.phoneNumber.phoneNumberId, this.contactForm.value).subscribe(() => {
      this.contactsService.fetchUserPhoneNumber(this.phoneNumber.phoneNumberId).toPromise().then((phoneNumber) => {
        this.toastr.success('Phone Number Updated Successfully');
        this.contactForm.reset(this.contactForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }


  onChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedCountry = this.countries.find(country => country.countryId == id);
  }
}
