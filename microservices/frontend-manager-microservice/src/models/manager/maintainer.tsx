import {CandidateCv} from "../cv/candidate-cv";

export interface Maintainer{
    cartMaintainerId: number;
    cartId: number;
    maintainerId: number;
    role: string;
    maintainer: CandidateCv;
}
