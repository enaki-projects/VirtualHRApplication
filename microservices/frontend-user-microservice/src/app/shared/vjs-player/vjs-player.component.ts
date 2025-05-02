// vjs-player.component.ts
import {Component, ElementRef, Input, OnDestroy, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import videojs from 'video.js';
import {VjsPlayerSettings} from './model/VjsPlayerSettings';
import {AvatarService} from '../../_services/avatar.service';
import {Subscription} from 'rxjs';
import {AvatarVideoSettings} from './model/AvatarVideoSettings';

@Component({
  selector: 'app-vjs-player',
  templateUrl: './vjs-player.component.html',
  styleUrls: [
    './vjs-player.component.css'
  ],
  encapsulation: ViewEncapsulation.None,
})

export class VjsPlayerComponent implements OnInit, OnDestroy {
  @ViewChild('target', {static: true}) target: ElementRef;
  // see options: https://github.com/videojs/video.js/blob/maintutorial-options.html
  @Input() options: VjsPlayerSettings;
  @Input() subtitles: string;
  player: videojs.Player;
  subscriptions: Subscription[] = [];
  avatarSettings: AvatarVideoSettings;
  kkt = false;

  constructor(
    private elementRef: ElementRef,
    private avatarService: AvatarService,
  ) {
  }

  ngOnInit(): void {
    // instantiate Video.js

    this.subscriptions.push(this.avatarService.videoSettings$.subscribe(settings => {
      this.options = settings;
      console.log(settings);
      if (this.avatarSettings && this.avatarSettings.enabled && this.player) {
        this.player.src(this.options.sources[0]);
        this.player.play();
      }
    }));
    this.subscriptions.push(this.avatarService.videoSubtitle$.subscribe(subtitles => {
      this.subtitles = subtitles;
    }));

    this.subscriptions.push(this.avatarService.avatarSettings$.subscribe(avatarSettings => {
      this.avatarSettings = avatarSettings;
    }));

    if (this.avatarSettings.enabled) {
      this.player = videojs(this.target.nativeElement, this.options, function onPlayerReady() {
        // console.log('onPlayerReady', this);
      });
    }
  }

  ngOnDestroy(): void {
    // destroy player
    if (this.player) {
      this.player.dispose();
    }
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }
}
