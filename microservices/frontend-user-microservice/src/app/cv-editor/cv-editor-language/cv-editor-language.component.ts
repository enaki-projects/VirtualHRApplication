import {Component, Input, OnInit} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {ToastrService} from 'ngx-toastr';
import {LanguageService} from '../../_services/cv-references/language.service';
import {UserLanguage} from '../../_models/cv/references/user-language';
import {CvEditorLanguageModalComponent} from '../_modals/cv-editor-language-modal/cv-editor-language-modal.component';

@Component({
  standalone: false,
  selector: 'app-cv-editor-language',
  templateUrl: './cv-editor-language.component.html',
  styleUrls: ['./cv-editor-language.component.css']
})
export class CvEditorLanguageComponent implements OnInit {
  @Input() userLanguages: UserLanguage[];
  faPen = faPen;
  faTrash = faTrash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private languageService: LanguageService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteLanguage(languageId: number): void {
    this.languageService.deleteLanguage(languageId).toPromise().then(result => {
      this.languageService.fetchDeletedLanguage(languageId);
      this.toastr.info('Language Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(userLanguage: UserLanguage): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        userLanguage,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorLanguageModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorLanguageModalComponent, config);
  }

}
