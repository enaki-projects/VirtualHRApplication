import {Component, OnDestroy, OnInit} from '@angular/core';
import {AuthenticatedUser} from '../_models/authenticated-user';
import {Subscription} from 'rxjs';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../_services/account.service';
import {AvatarService} from '../_services/avatar.service';
import {environment} from '../../environments/environment';
import html2canvas from 'html2canvas';
import { jsPDF  } from 'jspdf';
import {faDownload, faEye, faPlus, faMinus} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-cv-view',
  templateUrl: './cv-view.component.html',
  styleUrls: ['./cv-view.component.css']
})
export class CvViewComponent implements OnInit, OnDestroy {
  faCloud = faDownload;
  faEye = faEye;
  faPlus = faPlus;
  faMinus = faMinus;
  currentUser: AuthenticatedUser;
  subscriptions: Subscription[] = [];
  moreOptions = false;

  constructor(
    private toastr: ToastrService,
    private accountService: AccountService,
    public avatarService: AvatarService
  ) {
  }

  ngOnInit(): void {
    console.log('[CV] - OnInit');
    this.avatarService.updateAvatarVideo(environment.avatarScenarios.cv_view_page).subscribe((result) => {
      // console.log(result);
    });

    this.loadCurrentUser();
  }

  enableMoreOptions(): void{
    this.moreOptions = !this.moreOptions;
  }

  loadCurrentUser(): void {
    const subscription = this.accountService.currentUser$.subscribe(currentUser => {
      this.currentUser = currentUser;
    });
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    console.log('[CV] - OnDestroy');
    this.subscriptions.forEach((subscription) => {
      subscription.unsubscribe();
    });
  }

  captureScreen(view = true): void
  {
    const data = document.getElementById('cvToConvert');
    html2canvas(data, {useCORS: true}).then(canvas => {
      const noItems: number = this.currentUser.cv.cvReferences.projects.length +
        this.currentUser.cv.cvReferences.awards.length +
        this.currentUser.cv.cvReferences.professionalExperiences.length +
        this.currentUser.cv.cvReferences.educationalExperiences.length +
        this.currentUser.cv.cvReferences.publications.length;
      const imgWidth = 208;

      let imgHeight = 295;
      if (noItems < 2){
        imgHeight = 150;
      }
      else if (noItems < 4){
        imgHeight = 200;
      }
      console.log(canvas);
      const contentDataURL = canvas.toDataURL('image/png');
      const pdf = new jsPDF ('p', 'mm', 'a4');

      pdf.addImage(contentDataURL, 'PNG', 0, 0, imgWidth, imgHeight);
      if (view) {
        window.open(URL.createObjectURL(pdf.output('blob')));
      } else {
        pdf.save(`CV_${this.currentUser.cv.firstname}_${this.currentUser.cv.lastname}.pdf`);
      }
    });
  }

}
