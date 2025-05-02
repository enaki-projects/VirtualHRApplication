import {City} from './city';

export interface Organisation {
  organisationId: number;
  organisationName: string;
  address: string;
  city: City;
}
