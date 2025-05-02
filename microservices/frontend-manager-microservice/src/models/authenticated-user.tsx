import {ManagerCv} from "./cv/manager-cv";

export interface AuthenticatedUser {
    uid: string;
    email: string;
    token: string;
    urlImage: string;
    cv: ManagerCv;
}

