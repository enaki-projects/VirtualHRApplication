import {CvReferences} from "./cv-references";

export interface CandidateCv{
    uid: number;
    firstname: string;
    lastname: string;
    email: string;
    gender: string;
    birthdate: Date;
    aboutMe: string;
    motto: string;
    iqScore: number;
    hasImage: boolean;
    canTakeTheIqRequest: boolean;
    requestedRetakeOfIqTest: boolean;
    cvReferences: CvReferences;
}

