import {EducationalInstitution} from "../../general/educational-institution";

export interface EducationalExperience {
  educationalExperienceId: number;
  qualificationTitle: string;
  specialty: string;
  fromDate: string;
  toDate: string;
  educationalInstitution: EducationalInstitution;
}
