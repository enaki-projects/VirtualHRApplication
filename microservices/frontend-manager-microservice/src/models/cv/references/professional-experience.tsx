import {Organisation} from "../../general/organisation";

export interface ProfessionalExperience {
  professionalExperienceId: number;
  jobName: string;
  description: string;
  fromDate: string;
  toDate: string;
  organisation: Organisation;
}
