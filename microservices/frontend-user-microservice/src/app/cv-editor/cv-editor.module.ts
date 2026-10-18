import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {CvEditorRoutingModule} from './cv-editor-routing.module';
import {CvEditorComponent} from './cv-editor.component';
import {CvEditorGeneralInfoComponent} from './cv-editor-general-info/cv-editor-general-info.component';
import {SharedModule} from '../shared/shared.module';
import {CvEditorContactsComponent} from './cv-editor-contacts/cv-editor-contacts.component';
import {CvEditorContactModalComponent} from './_modals/cv-editor-contact-modal/cv-editor-contact-modal.component';
import {CvEditorNationalityComponent} from './cv-editor-nationality/cv-editor-nationality.component';
import {CvEditorNationalityModalComponent} from './_modals/cv-editor-nationality-modal/cv-editor-nationality-modal.component';
import {CvEditorLanguageComponent} from './cv-editor-language/cv-editor-language.component';
import {CvEditorLanguageModalComponent} from './_modals/cv-editor-language-modal/cv-editor-language-modal.component';
import {CvEditorLocationComponent} from './cv-editor-location/cv-editor-location.component';
import {CvEditorLocationModalComponent} from './_modals/cv-editor-location-modal/cv-editor-location-modal.component';
import {CvEditorHobbyComponent} from './cv-editor-hobby/cv-editor-hobby.component';
import {CvEditorHobbyModalComponent} from './_modals/cv-editor-hobby-modal/cv-editor-hobby-modal.component';
import {CvEditorProjectModalComponent} from './_modals/cv-editor-project-modal/cv-editor-project-modal.component';
import {CvEditorProjectComponent} from './cv-editor-project/cv-editor-project.component';
import {CvEditorPublicationComponent} from './cv-editor-publication/cv-editor-publication.component';
import {CvEditorPublicationModalComponent} from './_modals/cv-editor-publication-modal/cv-editor-publication-modal.component';
import {CvEditorAwardComponent} from './cv-editor-award/cv-editor-award.component';
import {CvEditorAwardModalComponent} from './_modals/cv-editor-award-modal/cv-editor-award-modal.component';
import {CvEditorEducationalExperienceComponent} from './cv-editor-educational-experience/cv-editor-educational-experience.component';
import {CvEditorEducationalExperienceModalComponent} from './_modals/cv-editor-educational-experience-modal/cv-editor-educational-experience-modal.component';
import {FormsModule} from '@angular/forms';
import {TypeaheadModule} from 'ngx-bootstrap/typeahead';
import {CvEditorProfessionalExperienceComponent} from './cv-editor-professional-experience/cv-editor-professional-experience.component';
import {CvEditorProfessionalExperienceModalComponent} from './_modals/cv-editor-professional-experience-modal/cv-editor-professional-experience-modal.component';
import {CvEditorSkillsComponent} from './cv-editor-skills/cv-editor-skills.component';


@NgModule({
  declarations: [
    CvEditorComponent,
    CvEditorGeneralInfoComponent,
    CvEditorContactsComponent,
    CvEditorContactModalComponent,
    CvEditorNationalityComponent,
    CvEditorNationalityModalComponent,
    CvEditorLanguageComponent,
    CvEditorLanguageModalComponent,
    CvEditorLocationComponent,
    CvEditorLocationModalComponent,
    CvEditorHobbyComponent,
    CvEditorHobbyModalComponent,
    CvEditorProjectModalComponent,
    CvEditorProjectComponent,
    CvEditorPublicationComponent,
    CvEditorPublicationModalComponent,
    CvEditorAwardComponent,
    CvEditorAwardModalComponent,
    CvEditorEducationalExperienceComponent,
    CvEditorEducationalExperienceModalComponent,
    CvEditorProfessionalExperienceComponent,
    CvEditorProfessionalExperienceModalComponent,
    CvEditorSkillsComponent
  ],
    imports: [
        CvEditorRoutingModule,
        FormsModule,
        TypeaheadModule,
        CommonModule,
        SharedModule,
    ]
})
export class CvEditorModule { }
