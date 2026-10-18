import {BehaviorSubject} from 'rxjs';
import {AuthenticatedUser} from "../models/authenticated-user";
import {LoginUser} from "../models/login-user";
import axios from "axios";
import {NotificationManager} from '../shared/notifications';

import {config} from "../environments/config";
import {avatarService} from "./AvatarService";

const environment = config.environment;


let currentUserSource: BehaviorSubject<AuthenticatedUser | null> = new BehaviorSubject(getCurrentUserFromLocalStorage());
let currentUser: AuthenticatedUser = getCurrentUserFromLocalStorage();

export const authenticationService: any = {
    login,
    logout,
    authHeader,
    getCurrentUserFromLocalStorage,
    getUserPhotoUrl,
    currentUser$: currentUserSource.asObservable(),
    get currentUserValue () { return currentUserSource.value }
};

function authHeader(){
    return  {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${currentUser.token}`,
    }
}

function getUserImageAccordingToGender(gender: string): string{
    console.log(gender);
    switch (gender.toLowerCase()) {
        case 'male':
            return `${import.meta.env.BASE_URL}assets/images/male.png`;
        case 'female':
            return  `${import.meta.env.BASE_URL}assets/images/female.png`;
        default:
            return  `${import.meta.env.BASE_URL}assets/images/user.png`;
    }
}


function setCurrentUser(user: AuthenticatedUser): void {
    if (!user.cv.hasImage){
        user.urlImage = getUserImageAccordingToGender(user.cv.gender);
    }
    localStorage.setItem('user', JSON.stringify(user));
    currentUserSource.next(user);
}

function getCurrentUserFromLocalStorage(): AuthenticatedUser {
    let tempUser = localStorage.getItem('user');
    return (tempUser == null)? null: JSON.parse(tempUser);
}

function logout(): void {
    localStorage.removeItem('user');
    sessionStorageCleaning();
    NotificationManager.success('Logout Successful');
}

function login(credentials: LoginUser) {
    const requestOptions = {
        url: `${environment.adapterApiUrl}auth/login`,
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        data: credentials
    };
    console.log(credentials)
    return axios.post(`${environment.adapterApiUrl}auth/login?as=manager`, credentials)
        .then((response) => {
            console.log(response)
            currentUser = response.data;
            return fetchUserCv();
        })
}

function fetchUserCv(){
    const uid = currentUser.uid;
    return axios.get(`${environment.adapterApiUrl}cvs/${uid}`, {headers: authHeader()})
        .then(response => {
            avatarService.resetAvatarSettings();
            currentUser.cv = response.data;
            if (currentUser.cv.hasImage){
                currentUser.urlImage = `${environment.adapterApiUrl}users/${currentUser.uid}/image`;
            } else {
                currentUser.urlImage = getUserImageAccordingToGender(currentUser.cv.gender);
            }
            setCurrentUser(currentUser);
        })
}

function getUserPhotoUrl(hasImage: boolean, uid: number, gender: string){
    if (hasImage){
        return `${environment.adapterApiUrl}users/${uid}/image`;
    } else {
        return getUserImageAccordingToGender(gender);
    }
}


function sessionStorageCleaning(){
    sessionStorage.clear();
    currentUserSource.next(null);
}
