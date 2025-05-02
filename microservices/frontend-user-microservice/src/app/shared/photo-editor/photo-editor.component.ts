import {Component, OnInit} from '@angular/core';
import {FileUploader} from 'ng2-file-upload';
import {environment} from '../../../environments/environment';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {AccountService} from '../../_services/account.service';
import {ToastrService} from 'ngx-toastr';
import {faBan, faCloudUploadAlt, faTrash, faUpload} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-photo-editor',
  templateUrl: './photo-editor.component.html',
  styleUrls: ['./photo-editor.component.css']
})
export class PhotoEditorComponent implements OnInit {
  uploader: FileUploader;
  hasBaseDropzoneOver = false;
  baseUrl = environment.adapterApiUrl;
  user: AuthenticatedUser;
  faTrash = faTrash;
  faBan = faBan;
  faUpload = faUpload;
  faCloudUploadAlt = faCloudUploadAlt;

  constructor(private accountService: AccountService, private toastr: ToastrService) {
    this.accountService.currentUser$.subscribe((user) => {
      this.user = user;
    });
  }

  ngOnInit(): void {
    this.initializeUploader();
  }

  fileOverBase(e: any): void {
    this.hasBaseDropzoneOver = e;
  }


  initializeUploader(): void {
    this.uploader = new FileUploader({
      url: this.baseUrl + `users/${this.user.uid}/image`,
      authToken: 'Bearer ' + this.user.token,
      isHTML5: true,
      // allowedFileType: ['image'],
      allowedMimeType: ['image/png', 'image/gif', 'video/mp4', 'image/jpeg'],
      removeAfterUpload: true,
      autoUpload: false,
      maxFileSize: 10 * 1024 * 1024

    });


    this.uploader.onAfterAddingFile = (file) => {
      file.withCredentials = false;
      if (this.uploader.queue.length > 1) {
        this.uploader.removeFromQueue(this.uploader.queue[0]);
      }
    };

    this.uploader.onSuccessItem = (item, response, status, headers) => {
      if (status === 200) {
        this.toastr.success('Image has been updated successfully!');
        this.accountService.refreshUserImage();
      }
    };

    this.uploader.onErrorItem = (item, response, status, headers) => {
      this.toastr.error('Error on updating the image.');
    };
  }
}
