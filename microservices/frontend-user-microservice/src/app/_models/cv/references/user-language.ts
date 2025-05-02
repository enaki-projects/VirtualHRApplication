import {Language} from '../general/language';

export interface UserLanguage {
  userLanguageId: number;
  listening: string;
  reading: string;
  spokenInteraction: string;
  spokenProduction: string;
  writing: string;
  language: Language;
}
