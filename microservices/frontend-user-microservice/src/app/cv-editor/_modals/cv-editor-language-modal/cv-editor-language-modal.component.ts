import {Component, OnInit} from '@angular/core';
import {UserLanguage} from '../../../_models/cv/references/user-language';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Language} from '../../../_models/cv/general/language';
import {faFlag, faGlobe, faPhoneAlt} from '@fortawesome/free-solid-svg-icons';
import {ToastrService} from 'ngx-toastr';
import {GeographyService} from '../../../_services/geography.service';
import {BsModalRef} from 'ngx-bootstrap/modal';
import {LanguageService} from '../../../_services/cv-references/language.service';

@Component({
  standalone: false,
  selector: 'app-cv-editor-language-modal',
  templateUrl: './cv-editor-language-modal.component.html',
  styleUrls: ['./cv-editor-language-modal.component.css']
})
export class CvEditorLanguageModalComponent implements OnInit {
  userLanguage: UserLanguage = null;
  addAction = false;
  updateAction = false;

  languageForm: FormGroup;
  languages: Language[];
  selectedLanguage: Language;
  faPhoneAlt = faPhoneAlt;
  faFlag = faFlag;
  faGlobe = faGlobe;
  languageLevels = ['A1', 'A2', 'B1', 'B2', 'C1', 'C2'];

  constructor(
    private fb: FormBuilder,
    private languageService: LanguageService,
    private toastr: ToastrService,
    private geographyService: GeographyService,
    public activeModal: BsModalRef
  ) {
    console.log('[CvEditorLanguageModalComponent] - constructor');
    this.languageForm = this.fb.group({
      languageId: ['', [Validators.required]],
      listening: ['', [Validators.required]],
      reading: ['', [Validators.required]],
      spokenInteraction: ['', [Validators.required]],
      spokenProduction: ['', [Validators.required]],
      writing: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    console.log('[CvEditorModal] - OnInit');
    this.geographyService.getLanguages().then((languages => {
      this.languages = languages;
      this.initializeForm();
    }), error => {
      console.log(error);
      if (error.error && error.error.status === 401){
        this.activeModal.hide();
      }
    });
  }

  initializeForm(): void {
    if (this.updateAction) {
      this.languageForm = this.fb.group({
        languageId: [this.userLanguage.language.languageId, [Validators.required]],
        listening: [this.userLanguage.listening, [Validators.required]],
        reading: [this.userLanguage.reading, [Validators.required]],
        spokenInteraction: [this.userLanguage.spokenInteraction, [Validators.required]],
        spokenProduction: [this.userLanguage.spokenProduction, [Validators.required]],
        writing: [this.userLanguage.writing, [Validators.required]],
      });
      this.selectedLanguage = this.userLanguage.language;
    } else if (this.addAction) {
      this.selectedLanguage = this.languages.find(language => language.code === 'en');
      this.languageForm = this.fb.group({
        languageId: [this.selectedLanguage.languageId, [Validators.required]],
        listening: ['', [Validators.required]],
        reading: ['', [Validators.required]],
        spokenInteraction: ['', [Validators.required]],
        spokenProduction: ['', [Validators.required]],
        writing: ['', [Validators.required]],
      });
    }
  }

  buttonCallback(): void{
    if (this.addAction){
      this.addLanguage();
    } else if (this.updateAction){
      this.updateLanguage();
    }
  }

  addLanguage(): void{
    this.languageService.addLanguage(this.languageForm.value).subscribe(userLanguageId => {
      this.languageService.fetchUserLanguage(userLanguageId).toPromise().then((language) => {
        this.toastr.success('Language Added Successfully');
        this.languageForm.reset(this.languageForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  updateLanguage(): void{
    this.languageService.updateLanguage(this.userLanguage.userLanguageId, this.languageForm.value).subscribe(() => {
      this.languageService.fetchUserLanguage(this.userLanguage.userLanguageId).toPromise().then((language) => {
        this.toastr.success('Language Updated Successfully');
        this.languageForm.reset(this.languageForm.value);
        this.activeModal.hide();
      });
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }


  onChange(id: number): void {
    // tslint:disable-next-line:triple-equals
    this.selectedLanguage = this.languages.find(language => language.languageId == id);
  }
}
