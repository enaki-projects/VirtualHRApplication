import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable, ReplaySubject} from 'rxjs';
import {VjsPlayerSettings} from '../shared/vjs-player/model/VjsPlayerSettings';
import {AvatarVideoScenario} from '../shared/vjs-player/model/AvatarVideoScenario';
import {map} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {AvatarVideoInfo} from '../shared/vjs-player/model/AvatarVideoInfo';
import {AvatarVideoSettings} from '../shared/vjs-player/model/AvatarVideoSettings';
import { AccountService } from './account.service';

@Injectable({
  providedIn: 'root'
})
export class AvatarService {
  private videoSubtitleSource = new ReplaySubject<string>(1);
  videoSubtitle$ = this.videoSubtitleSource.asObservable();

  private videoSettingsSource = new ReplaySubject<VjsPlayerSettings>(1);
  videoSettings$ = this.videoSettingsSource.asObservable();

  private avatarSettingsSource = new ReplaySubject<AvatarVideoSettings>(1);
  avatarSettings$ = this.avatarSettingsSource.asObservable();

  subtitle = '';
  settings: VjsPlayerSettings = {
    fluid: true,
    autoplay: true,
    controls: false,
    muted: false,
    sources: [{
      src: `${environment.avatarApiUrl}avatar?name=cv_page_1&language=en&category=cv&resolution=720x720&subcategory=page`,
      type: 'video/mp4'
    }]
  };

  avatarSettings: AvatarVideoSettings;
  subscriptions = [];

  constructor(private http: HttpClient,
              private accountService: AccountService) {
    this.videoSettingsSource.next(this.settings);
    this.videoSubtitleSource.next(this.subtitle);
    this.setDefaultAvatarSettings();
  }

  setDefaultAvatarSettings(): void{
    this.avatarSettings = {
      language: 'en',
      resolution: '720x720',
      enabled: true,
      enableSubtitles: true
    };
    this.avatarSettingsSource.next(this.avatarSettings);
  }

  updateSettings(language: string, resolution: string, enabled: boolean, enableSubtitles: boolean): void{
    this.avatarSettings.language = language;
    this.avatarSettings.resolution = resolution;
    this.avatarSettings.enabled = enabled;
    this.avatarSettings.enableSubtitles = enableSubtitles;
    this.avatarSettingsSource.next(this.avatarSettings);
  }

  getSettings(): any{
    return this.settings;
  }

  updateAvatarVideo(scenario: AvatarVideoScenario): Observable<any>{
    const avatarInfoUrl = this.getCurrentUrl(scenario);
    return this.http.get<AvatarVideoInfo>(avatarInfoUrl).pipe(
      map((videoInfo: AvatarVideoInfo) => {
        this.settings.sources[0].src = `${environment.avatarApiUrl}${videoInfo.videoQuery}`;
        this.subtitle = videoInfo.textToSpeech;
        this.videoSettingsSource.next(this.settings);
        this.videoSubtitleSource.next(this.subtitle);
        // console.log(videoInfo);
      })
    );
  }

  private getCurrentUrl(scenario: AvatarVideoScenario): string{
    return `${environment.avatarApiUrl}avatar-info?name=${scenario.name}&subcategory=${scenario.subcategory}&category=${scenario.category}&language=${this.avatarSettings.language}&resolution=${this.avatarSettings.resolution}`;
  }
}
