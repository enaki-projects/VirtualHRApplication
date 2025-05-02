import {Country} from './country';

export interface EducationalInstitution {
  educationalInstitutionId: number;
  institutionName: string;
  altInstitutionName: string;
  address: string;
  telephone: string;
  website: string;
  email: string;
  funding: string;
  accreditingAgency: string;
  country: Country;
}
