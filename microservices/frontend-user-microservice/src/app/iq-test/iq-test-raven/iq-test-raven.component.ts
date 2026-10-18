import {Component, HostListener, OnInit, ViewChild} from '@angular/core';
import {IqService} from '../../_services/iq/iq.service';
import {ToastrService} from 'ngx-toastr';
import {RavenQuiz} from '../../_models/iq/raven-apm/raven-quiz';
import {RavenSection} from '../../_models/iq/raven-apm/raven-section';
import {faAngleRight} from '@fortawesome/free-solid-svg-icons';
import {RavenAnswer} from '../../_models/iq/raven-apm/raven-answer';
import {Router} from '@angular/router';
import {CountdownComponent} from 'ngx-countdown';

@Component({
  standalone: false,
  selector: 'app-iq-test-raven',
  templateUrl: './iq-test-raven.component.html',
  styleUrls: ['./iq-test-raven.component.css']
})
export class IqTestRavenComponent implements OnInit {
  ravenQuiz: RavenQuiz;
  currentSection: RavenSection;
  currentSectionIdx = 0;
  faAngleRight = faAngleRight;
  userResponse = {};
  currentQuestion = 1;
  myConfig = {
    leftTime: 2700,
  };
  myInput: any = null;
  @HostListener('window:beforeunload', ['$event']) unloadNotification($event: any): void {
    $event.returnValue = true;
    this.iqService.submitUserRavenQuizResponse();
  }

  constructor(
    private iqService: IqService,
    private toastr: ToastrService,
    private router: Router
  ) {
    console.log('[IqTestRavenComponent] - constructor');

  }

  ngOnInit(): void {
    console.log('[IqTestRavenComponent] - OnInit');
    this.iqService.getRavenQuiz().toPromise().then((ravenQuiz => {
      // console.log(ravenQuiz);
      this.ravenQuiz = ravenQuiz;
      this.currentSectionIdx = 0;
      this.currentSection = ravenQuiz.A;
      this.iqService.startTheIqTest();
    }));
  }


  getSectionName(): string{
    switch (this.currentSectionIdx) {
      case 0: return 'Section A';
      case 1: return 'Section B';
      case 2: return 'Section C';
      case 3: return 'Section D';
      default: return 'Section E';
    }
  }

  onQuestionAnswerClick($event: any): void {
    const questionCode = $event.target.name;
    // console.log($event);
    const questionResponse = $event.target.value;
    this.userResponse[questionCode] = questionResponse;
    // console.log(this.userResponse);
  }

  getNumberOfAnswers(): number {
    const keys = Object.keys(this.userResponse);
    return keys.length;
  }

  getCurrentQuestion(): number {
    return this.currentSectionIdx * 12 + this.currentQuestion;
  }

  moveToNextSection(): void {
    this.currentSectionIdx += 1;
    switch (this.currentSectionIdx){
      case 1: this.currentSection = this.ravenQuiz.B; break;
      case 2: this.currentSection = this.ravenQuiz.C; break;
      case 3: this.currentSection = this.ravenQuiz.D; break;
      case 4: this.currentSection = this.ravenQuiz.E; break;
    }
    window.scroll(0, 0);
  }

  moveToNextQuestion(): void {
    if (this.currentQuestion === 12){
      this.moveToNextSection();
      this.currentQuestion = 1;
    } else {
      this.currentQuestion += 1;
    }
    // this.userResponse[this.currentSection.questions[this.currentQuestion - 1]] = -1;
    // console.log(this.userResponse);
    window.scroll(0, 0);
  }

  submitIqResponses(): void {
    // console.log(this.userResponse);
    const ravenAnswers: RavenAnswer[] = [];
    Object.keys(this.userResponse).forEach((key, index) => {
      const ravenAnswer: RavenAnswer = {
        Question: key,
        Chosen: this.userResponse[key]
      };
      ravenAnswers.push(ravenAnswer);
    });
    this.iqService.appendUserResponses(ravenAnswers);
    this.iqService.submitUserRavenQuizResponse().toPromise().then((response) => {
      this.toastr.info('Iq Test Submitted Successfully');
      this.router.navigate(['iq-test']);
    }, error => {
      console.log(error);
      this.toastr.error(error.error.status, error.error.error);
    });
  }

  getCurrentSectionLabels(): number[]{
    const currentLabels = [];
    for (let i = 1; i <= this.currentSection.total; i++){
      currentLabels.push(i);
    }
    return currentLabels;
  }

  handleLimitTime($event: any): void {
    if ($event.action === 'done'){
      this.submitIqResponses();
    }
  }
}
