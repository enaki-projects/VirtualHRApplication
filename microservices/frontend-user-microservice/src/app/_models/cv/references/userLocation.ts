import {City} from '../general/city';

export interface UserLocation {
  locationId: number;
  type: string;
  address: string;
  city: City;
}
