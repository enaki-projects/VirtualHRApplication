import {authenticationService} from "./AuthService";
import axios from "axios";

import {config} from "../environments/config";
const environment = config.environment;

function fetchCandidatesCv(): Promise<any>{
    return axios.get(`${environment.adapterApiUrl}cvs`, {headers: authenticationService.authHeader()});
}

function fetchCandidatesCvById(id: number): Promise<any>{
    return axios.get(`${environment.adapterApiUrl}cvs/${id}`, {headers: authenticationService.authHeader()});
}

function fetchCandidatesCvByCriteria(criteria): Promise<any>{
    console.log(criteria);
    return axios.post(`${environment.adapterApiUrl}cvs/filter`,criteria, {headers: authenticationService.authHeader()});
}

export const candidateService: any = {
    fetchCandidatesCv,
    fetchCandidatesCvById,
    fetchCandidatesCvByCriteria
};
