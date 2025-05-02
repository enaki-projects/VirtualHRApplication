import {CV} from './cv/cv';

export interface AuthenticatedUser {
  uid: string;
  email: string;
  token: string;
  urlImage: string;
  cv: CV;
}

