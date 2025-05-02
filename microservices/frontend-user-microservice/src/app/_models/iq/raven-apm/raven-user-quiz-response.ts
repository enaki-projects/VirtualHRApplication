import {RavenAnswer} from './raven-answer';

export interface RavenUserQuizResponse{
  Answers: RavenAnswer[];
  Started: Date;
  Finished: Date;
  Age: number;
}
