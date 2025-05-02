import {Country} from '../general/country';

export interface PhoneNumber {
  phoneNumberId: number;
  number: string;
  country: Country;
}
