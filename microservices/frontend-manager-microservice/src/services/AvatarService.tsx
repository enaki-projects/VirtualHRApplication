import {BehaviorSubject, ReplaySubject} from "rxjs";
import {VjsPlayerSettings} from "../components/shared/Avatar/models/VjsPlayerSettings";
import {AvatarVideoScenario} from "../components/shared/Avatar/models/AvatarVideoScenario";
import {authenticationService} from "./AuthService";
import {AvatarVideoInfo} from "../components/shared/Avatar/models/AvatarVideoInfo";

import {config} from "../environments/config";
import {AvatarVideoSettings} from "../components/shared/Avatar/models/AvatarVideoSettings";
const environment = config.environment;

let subtitle = '';
let settings: VjsPlayerSettings = {
    playing: true,
    controls: false,
    muted: false,
    url: ''
};

let avatarSettings = {
    language: 'en',
    resolution: '720x720',
    enabled: true,
    enableSubtitles: true
};

if (sessionStorage.getItem('avatarSettings')){
    avatarSettings = JSON.parse(sessionStorage.getItem('avatarSettings'));
}

let videoSubtitleSource = new BehaviorSubject<string>(subtitle);
let videoSubtitle$ = videoSubtitleSource.asObservable();

let videoSettingsSource = new BehaviorSubject<VjsPlayerSettings>(settings);
let videoSettings$ = videoSettingsSource.asObservable();

let avatarSettingsSource = new ReplaySubject<AvatarVideoSettings>(1);
let avatarSettings$ = avatarSettingsSource.asObservable();


function resetAvatarSettings(){
    avatarSettings = {
        language: 'en',
        resolution: '720x720',
        enabled: true,
        enableSubtitles: true
    };
    sessionStorage.setItem("avatarSettings", JSON.stringify(avatarSettings))
    avatarSettingsSource.next(avatarSettings);
}

function getCurrentUrl(scenario: AvatarVideoScenario): string {
    return `${environment.avatarApiUrl}avatar-info?name=${scenario.name}&subcategory=${scenario.subcategory}&category=${scenario.category}&language=${avatarSettings.language}&resolution=${avatarSettings.resolution}`;
}

function updateAvatarVideo(scenario: AvatarVideoScenario): Promise<any>{
    const avatarInfoUrl = getCurrentUrl(scenario);
    return fetch(avatarInfoUrl, {
        method: 'GET',
        headers: authenticationService.authHeader(),
    })
        .then(data => data.json())
        .then((videoInfo: AvatarVideoInfo) => {
            settings.url = `${environment.avatarApiUrl}${videoInfo.videoQuery}`;
            subtitle = videoInfo.textToSpeech;
            videoSettingsSource.next(settings);
            videoSubtitleSource.next(subtitle);
        })
}

function updateAvatarSettings(avatarSettingsParam: AvatarVideoSettings): void{
    avatarSettings = avatarSettingsParam;
    avatarSettingsSource.next(avatarSettings);
    sessionStorage.setItem("avatarSettings", JSON.stringify(avatarSettings))
}

function getSettings(): AvatarVideoSettings{
    return avatarSettings;
}

export const avatarService: any = {
    videoSubtitle$,
    videoSettings$,
    avatarSettings$,
    updateAvatarVideo,
    updateAvatarSettings,
    getSettings,
    resetAvatarSettings
}
