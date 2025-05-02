import {Component, Input, OnInit} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {faEnvelopeSquare, faTable, faTrash, faUser, faVenusMars} from '@fortawesome/free-solid-svg-icons';
import {AccountService} from '../../_services/account.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-user-card',
  templateUrl: './user-card.component.html',
  styleUrls: ['./user-card.component.css']
})
export class UserCardComponent implements OnInit {
  @Input() currentUser: AuthenticatedUser;
  @Input() enableDeletePhotoButton = false;
  faUser = faUser;
  faEnvelopeSquare = faEnvelopeSquare;
  faVenusMars = faVenusMars;
  faTable = faTable;
  faTrash = faTrash;

  constructor(public accountService: AccountService,
              private toastr: ToastrService) {
    this.accountService.currentUser$.subscribe(user => this.currentUser = user);
  }

  ngOnInit(): void {
  }

  deletePhoto(): void{
    this.accountService.deleteUserPhoto().subscribe({
      next: data => {
        this.toastr.success('Photo Deleted Successfully');
      },
      error: error => {
        this.toastr.error('Cannot delete current photo');
      }
    });
  }
}
