import { Injectable } from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {Observable} from 'rxjs';
import {ConfirmDialogComponent} from '../../shared/_modals/confirm-dialog/confirm-dialog.component';

@Injectable({
  providedIn: 'root'
})
export class ConfirmService {

  bsModelRef: BsModalRef;

  constructor(private modalService: BsModalService) { }

  confirm(title = 'Confirmation',
          message = 'Are you sure you want to access this?',
          additionalInfos = [],
          btnOkText = 'Ok',
          btnCancelText = 'Cancel'): Observable<boolean> {
    const config = {
      initialState: {
        title,
        message,
        additionalInfos,
        btnOkText,
        btnCancelText
      }
    };
    this.bsModelRef = this.modalService.show(ConfirmDialogComponent, config);

    return new Observable<boolean>(this.getResult());
  }

  private getResult(): any {
    return (observer) => {
      const subscription = this.bsModelRef.onHidden.subscribe(() => {
        observer.next(this.bsModelRef.content.result);
        observer.complete();
      });

      return {
        unsubscribe(): void {
          subscription.unsubscribe();
        }
      };
    };
  }
}
