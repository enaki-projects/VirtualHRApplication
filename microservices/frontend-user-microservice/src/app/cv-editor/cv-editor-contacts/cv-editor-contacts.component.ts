import {Component, Input, OnInit} from '@angular/core';
import {PhoneNumber} from '../../_models/cv/references/phone-number';
import {faPen, faTrash} from '@fortawesome/free-solid-svg-icons';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {CvEditorContactModalComponent} from '../_modals/cv-editor-contact-modal/cv-editor-contact-modal.component';
import {ContactsService} from '../../_services/cv-references/contacts.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-cv-editor-contacts',
  templateUrl: './cv-editor-contacts.component.html',
  styleUrls: ['./cv-editor-contacts.component.css']
})
export class CvEditorContactsComponent implements OnInit {
  @Input() phoneNumbers: PhoneNumber[];
  faPen = faPen;
  faTrash = faTrash;
  bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService,
              private contactsService: ContactsService,
              private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
  }

  deleteNumber(phoneNumberId: number): void {
    this.contactsService.deletePhoneNumber(phoneNumberId).toPromise().then(result => {
      this.contactsService.fetchDeletedPhoneNumber(phoneNumberId);
      this.toastr.info('Phone Number Deleted Successfully');
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  openUpdateModal(phoneNumber: PhoneNumber): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        phoneNumber,
        updateAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorContactModalComponent, config);
  }

  openAddModal(): void {
    const config = {
      class: 'modal-dialog-centered',
      initialState: {
        addAction: true
      }
    };
    this.bsModalRef = this.modalService.show(CvEditorContactModalComponent, config);
  }
}
