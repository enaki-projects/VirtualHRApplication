import {Injectable} from '@angular/core';
import {AuthenticatedUser} from '../../_models/authenticated-user';
import {environment} from '../../../environments/environment';
import {AccountService} from '../account.service';
import {HttpClient} from '@angular/common/http';
import {RavenQuiz} from '../../_models/iq/raven-apm/raven-quiz';
import {Observable, ReplaySubject} from 'rxjs';
import {map} from 'rxjs/operators';
import {RavenUserQuizResponse} from '../../_models/iq/raven-apm/raven-user-quiz-response';
import moment from 'moment';
import {RavenAnswer} from '../../_models/iq/raven-apm/raven-answer';
import {RavenReview} from '../../_models/iq/raven-apm/raven-review';

@Injectable({
  providedIn: 'root'
})
export class IqService {
  private hasStartedTheTest = new ReplaySubject<boolean>(1);
  hasStartedTheTest$ = this.hasStartedTheTest.asObservable();

  constructor(
    private accountService: AccountService,
    private http: HttpClient
  ) {
    accountService.currentUser$.subscribe((user) => {
      this.currentUser = user;
    });
  }

  currentUser: AuthenticatedUser;
  baseUrl = environment.adapterApiUrl;
  currentUserRavenQuizResponse: RavenUserQuizResponse = {} as RavenUserQuizResponse;

  private static getAge(value: Date): number{
    const today = moment();
    const birthdate = moment(value);
    return today.diff(birthdate, 'years');
  }

  getLatestUserIqQuizResponse(): Observable<any> {
    return this.http.get(`${this.baseUrl}users/${this.currentUser.uid}/iq-tests?basicInfo=true&latest=true`).pipe(
      map((ravenQuiz: RavenReview) => {
        return ravenQuiz;
      }));
  }

  startTheIqTest(): void{
    this.hasStartedTheTest.next(true);
    this.currentUser.cv.canTakeTheIqRequest = false;
    this.accountService.setCurrentUser(this.currentUser);
  }

  getRavenQuiz(): Observable<any>{
    return this.http.get(`${this.baseUrl}raven-test?randomize=true`).pipe(
      map((ravenQuiz: RavenQuiz) => {
        ravenQuiz.A.sources = {};
        ravenQuiz.B.sources = {};
        ravenQuiz.C.sources = {};
        ravenQuiz.D.sources = {};
        ravenQuiz.E.sources = {};

        ravenQuiz.A.questions.forEach(questionCode => {
          ravenQuiz.A.sources[questionCode] = `${this.baseUrl}raven-test/${questionCode}`;
        });
        ravenQuiz.B.questions.forEach(questionCode => {
          ravenQuiz.B.sources[questionCode] = `${this.baseUrl}raven-test/${questionCode}`;
        });
        ravenQuiz.C.questions.forEach(questionCode => {
          ravenQuiz.C.sources[questionCode] = `${this.baseUrl}raven-test/${questionCode}`;
        });
        ravenQuiz.D.questions.forEach(questionCode => {
          ravenQuiz.D.sources[questionCode] = `${this.baseUrl}raven-test/${questionCode}`;
        });
        ravenQuiz.E.questions.forEach(questionCode => {
          ravenQuiz.E.sources[questionCode] = `${this.baseUrl}raven-test/${questionCode}`;
        });
        this.currentUserRavenQuizResponse.Age = IqService.getAge(this.currentUser.cv.birthdate);
        this.currentUserRavenQuizResponse.Started = moment().toDate();
        this.currentUserRavenQuizResponse.Answers = [];

        console.log('[fetchRavenAPM] - ' + this.currentUserRavenQuizResponse.Started);
        return ravenQuiz;
      })
    );
  }

  appendUserResponses(ravenAnswers: RavenAnswer[]): void{
    this.currentUserRavenQuizResponse.Answers.push(...ravenAnswers);
  }

  submitUserRavenQuizResponse(): Observable<any> {
    this.currentUserRavenQuizResponse.Finished = moment().toDate();
    return this.http.post(`${this.baseUrl}users/${this.currentUser.uid}/iq-tests`, this.currentUserRavenQuizResponse).pipe(
      map((iqTestResponseId: any) => {
        this.currentUser.cv.canTakeTheIqRequest = false;
        this.hasStartedTheTest.next(false);
        this.accountService.setCurrentUser(this.currentUser);
        console.log('[submitUserRavenQuizResponse] - ' + iqTestResponseId.Id);
        return iqTestResponseId.Id;
      })
    );
  }

  requestToRetakeTheTest(): Observable<any> {
    return this.http.post(`${this.baseUrl}users/${this.currentUser.uid}/iq-tests/request-permission`, null).pipe(
      map((response: any) => {
        this.currentUser.cv.requestedRetakeOfIqTest = true;
        this.accountService.setCurrentUser(this.currentUser);
        console.log('[requestToRetakeTheTest] - ' + response);
      })
    );
  }

  private resetUserRavenQuizResponse(): void{
    this.currentUserRavenQuizResponse.Age = undefined;
    this.currentUserRavenQuizResponse.Started = undefined;
    this.currentUserRavenQuizResponse.Finished = undefined;
    this.currentUserRavenQuizResponse.Answers = [];
  }
}
