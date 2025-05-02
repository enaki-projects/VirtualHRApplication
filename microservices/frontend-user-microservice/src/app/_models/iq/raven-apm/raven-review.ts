export interface RavenReview{
  iqTestId: number;
  uid: number;
  testName: string;
  exactIQ: number;
  iq: number;
  iqAdjustedByAge: number;
  timeTakenInSeconds: number;
  diagnostic: string;
  submittedOn: Date;
}
