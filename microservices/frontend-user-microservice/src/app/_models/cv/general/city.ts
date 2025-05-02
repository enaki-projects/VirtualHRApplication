import {Region} from './region';

export interface City {
  cityId: number;
  cityName: string;
  latitude: number;
  longitude: number;
  region: Region;
}
