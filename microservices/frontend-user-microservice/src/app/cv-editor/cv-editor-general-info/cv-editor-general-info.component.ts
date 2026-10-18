import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CvService} from '../../_services/cv.service';
import {ToastrService} from 'ngx-toastr';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {faEnvelope, faFileSignature, faKey, faUser, faVenusMars} from '@fortawesome/free-solid-svg-icons';


@Component({
  standalone: false,
  selector: 'app-cv-editor-general-info',
  templateUrl: './cv-editor-general-info.component.html',
  styleUrls: ['./cv-editor-general-info.component.css']
})
export class CvEditorGeneralInfoComponent implements OnInit {
  @Input() currentUser: AuthenticatedUser;
  generalInfoForm: FormGroup;
  faUser = faUser;
  faKey = faKey;
  faFileSignature = faFileSignature;
  faVenusMars = faVenusMars;
  faEnvelope = faEnvelope;
  maxDate: Date;

  constructor(
    private fb: FormBuilder,
    private cvService: CvService,
    private toastr: ToastrService,
  ) { }


  ngOnInit(): void {
    this.initializeForm();
    this.maxDate = new Date();
    this.maxDate.setFullYear(this.maxDate.getFullYear() - 15);
  }

  initializeForm(): void {
    console.log(this.currentUser);
    this.generalInfoForm = this.fb.group({
      email: [this.currentUser.cv.email, [Validators.required, Validators.email]],
      firstname: [this.currentUser.cv.firstname, [Validators.required]],
      lastname: [this.currentUser.cv.lastname, [Validators.required]],
      gender: [this.currentUser.cv.gender, [Validators.required, Validators.minLength(4)]],
      birthdate: [new Date(this.currentUser.cv.birthdate), [Validators.required]],
      motto: [this.currentUser.cv.motto],
      aboutMe: [this.currentUser.cv.aboutMe],
    });
  }

  updateGeneralInfo(): void{
    this.cvService.updateGeneralInfo(this.generalInfoForm.value).subscribe(response => {
      this.toastr.info('Profile Updated Successfully');
      this.generalInfoForm.reset(this.generalInfoForm.value);
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }
}
