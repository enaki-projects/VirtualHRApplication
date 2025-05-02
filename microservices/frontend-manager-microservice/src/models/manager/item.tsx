import {CandidateCv} from "../cv/candidate-cv";

export interface Item{
    cartItemId: number;
    candidateId: number;
    cartId: number;
    candidate: CandidateCv;
    score: number;
    notes: string;
}
