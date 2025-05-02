import {Award} from './references/award';
import {Citizenship} from './references/citizenship';
import {Hobby} from './references/hobby';
import {Project} from './references/project';
import {PhoneNumber} from './references/phone-number';
import {Publication} from './references/publication';
import {UserLanguage} from './references/user-language';
import {EducationalExperience} from './references/educational-experience';
import {ProfessionalExperience} from './references/professional-experience';
import {UserSkill} from './references/user-skill';
import {UserLocation} from './references/userLocation';

export interface CvReferences {
  locations: UserLocation[];
  citizenship: Citizenship[];
  awards: Award[];
  hobbies: Hobby[];
  projects: Project[];
  phoneNumbers: PhoneNumber[];
  publications: Publication[];
  iqTests: any[];
  userLanguages: UserLanguage[];
  educationalExperiences: EducationalExperience[];
  professionalExperiences: ProfessionalExperience[];
  userInterpersonalSkills: UserSkill[];
  userDigitalSkills: UserSkill[];
  userOrganisationalSkills: UserSkill[];
  userLeadershipSkills: UserSkill[];
}

