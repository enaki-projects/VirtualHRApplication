import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {UserCardComponent} from './user-card/user-card.component';
import {VjsPlayerComponent} from './vjs-player/vjs-player.component';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {ToastrModule} from 'ngx-toastr';
import {TabsModule} from 'ngx-bootstrap/tabs';
import {BsDatepickerModule} from 'ngx-bootstrap/datepicker';
import {FileUploadModule} from 'ng2-file-upload';
import {AgePipe} from './_pipes/age.pipe';
import {PhotoEditorComponent} from './photo-editor/photo-editor.component';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {DateInputComponent} from './_forms/date-input/date-input.component';
import {TextInputComponent} from './_forms/text-input/text-input.component';
import {ReactiveFormsModule} from '@angular/forms';
import {ModalModule} from 'ngx-bootstrap/modal';
import {SelectInputComponent} from './_forms/select-input/select-input.component';
import {SkillTagInputComponent} from './_forms/skill-tag-input/skill-tag-input.component';
import {FormsModule} from '@angular/forms';
import {ConfirmDialogComponent} from './_modals/confirm-dialog/confirm-dialog.component';


@NgModule({
  declarations: [
    UserCardComponent,
    VjsPlayerComponent,
    AgePipe,
    PhotoEditorComponent,
    DateInputComponent,
    TextInputComponent,
    SelectInputComponent,
    SkillTagInputComponent,
    ConfirmDialogComponent,
  ],
  imports: [
    CommonModule,
    BsDropdownModule,
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-center'
    }),
    FontAwesomeModule,
    TabsModule,
    ModalModule,
    BsDatepickerModule,
    FormsModule,
    ReactiveFormsModule,
    FileUploadModule,
  ],
  exports: [
    BsDropdownModule,
    ToastrModule,
    TabsModule,
    BsDatepickerModule,
    FileUploadModule,
    UserCardComponent,
    VjsPlayerComponent,
    DateInputComponent,
    AgePipe,
    TextInputComponent,
    PhotoEditorComponent,
    ReactiveFormsModule,
    FontAwesomeModule,
    ModalModule,
    SelectInputComponent,
    SkillTagInputComponent
  ],
})
export class SharedModule {

}
