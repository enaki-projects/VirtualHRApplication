import React from "react";
import {Subscription} from "rxjs";
import {avatarService} from "../../../services/AvatarService";
import {VjsPlayerSettings} from "./models/VjsPlayerSettings";
import {Card} from 'react-bootstrap';
import ReactPlayer from 'react-player'

import './AvatarVideoPlayer.css';
import {AvatarVideoSettings} from "./models/AvatarVideoSettings";


interface AvatarVideoState {
    options: VjsPlayerSettings | undefined,
    subtitles: string,
    avatarSettings: AvatarVideoSettings,
}

export default class AvatarVideoPlayer extends React.Component<any, AvatarVideoState> {
    player: any = {};
    _isMounted = false;
    subscriptions: Subscription[] = [];
    state: AvatarVideoState = {
        subtitles: '',
        options: undefined,
        avatarSettings: avatarService.getSettings()
    }

    constructor(props: any) {
        super(props);
        this.subscriptions.push(avatarService.videoSettings$.subscribe((settings: VjsPlayerSettings) => {
            if (this._isMounted)
            this.setState({
                options: settings,
            })
        }));
        this.subscriptions.push(avatarService.avatarSettings$.subscribe((avatarSettings: AvatarVideoSettings) => {
            if (this._isMounted)
                this.setState({
                avatarSettings: avatarSettings,
            })
        }));
        this.subscriptions.push(avatarService.videoSubtitle$.subscribe((subtitles: string) => {
            if (this._isMounted)
                this.setState({
                subtitles: subtitles
            })
        }));
    }

    componentDidMount() {
        this._isMounted = true;
        // instantiate Video.js
    }

    // destroy player on unmount
    componentWillUnmount() {
        this.subscriptions.forEach(subscription => {
            subscription.unsubscribe();
        })
        this.subscriptions = [];
    }

    render() {
        if (!this.state || typeof (this.state.options) === "undefined") {
            return <Card className="mb-4"/>
        }
        return (
            <Card className="mb-4">
                <Card.Header className="text-center bg-light">
                    <h3 className="mb-0 text-primary">Diana Vulpescu</h3>
                </Card.Header>
                <Card.Body>
                    {(this.state.avatarSettings.enabled) ?
                        <ReactPlayer className="react-player w-100 p-0 m-0" src={this.state.options.url} playing={this.state.options.playing} controls={this.state.options.controls} muted={this.state.options.muted}/>
                        : <img className="avatar-img" src={import.meta.env.BASE_URL + "assets/images/manager_avatar.png"} alt=""/>
                    }
                </Card.Body>
                {
                    (this.state.avatarSettings.enableSubtitles) ?
                        <Card.Footer className="text-center">
                            {this.state.subtitles}
                        </Card.Footer>
                        : null
                }
            </Card>

        )
    }
}
