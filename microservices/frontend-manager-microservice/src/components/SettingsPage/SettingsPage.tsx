import React from "react";
import {Col, Row} from "react-bootstrap";
import {avatarService} from "../../services/AvatarService";
import {environment} from "../../environments/environment";
import UserCard from "../shared/UserCard/UserCard";
import {AuthenticatedUser} from "../../models/authenticated-user";
import {authenticationService} from "../../services/AuthService";
import "./SettingsPage.scss"
import {NotificationManager} from '../../shared/notifications';


export class SettingsPage extends React.Component<any, any> {
    currentUser: AuthenticatedUser
    _isMounted = false;
    state = {
        avatarSettings: avatarService.getSettings()
    }

    constructor(props) {
        super(props);
        authenticationService.currentUser$.subscribe((user) => {
            this.currentUser = user;
        })

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChangeResolution = this.handleChangeResolution.bind(this);
        this.handleChangeLanguage = this.handleChangeLanguage.bind(this);
        this.handleChangeEnabled = this.handleChangeEnabled.bind(this);
        this.handleChangeEnableSubtitles = this.handleChangeEnableSubtitles.bind(this);
    }

    componentDidMount() {
        this._isMounted = true;
        avatarService.updateAvatarVideo(environment.avatarScenarios.home);
    }

    handleChangeResolution(event) {
        if (!this._isMounted) return;
        let currentAvatarSettings = this.state.avatarSettings;
        if (currentAvatarSettings !== undefined){
            currentAvatarSettings.resolution = event.target.value;
            this.setState({avatarSettings: currentAvatarSettings});
        }
    }

    handleChangeLanguage(event) {
        if (!this._isMounted) return;
        let currentAvatarSettings = this.state.avatarSettings;
        if (currentAvatarSettings !== undefined){
            currentAvatarSettings.language = event.target.value;
            this.setState({avatarSettings: currentAvatarSettings});
        }
    }

    handleChangeEnabled(event) {
        if (!this._isMounted) return;
        let currentAvatarSettings = this.state.avatarSettings;
        if (currentAvatarSettings !== undefined){
            currentAvatarSettings.enabled = event.target.value === "1";
            this.setState({avatarSettings: currentAvatarSettings});
        }
    }

    handleChangeEnableSubtitles(event) {
        if (!this._isMounted) return;
        let currentAvatarSettings = this.state.avatarSettings;
        if (currentAvatarSettings){
            currentAvatarSettings.enableSubtitles = event.target.value === "1";
            this.setState({avatarSettings: currentAvatarSettings});
        }
        console.log(this.state);

    }

    handleSubmit(event) {
        event.preventDefault();
        if (!this._isMounted) return;
        avatarService.updateAvatarSettings(this.state.avatarSettings);
        NotificationManager.success("Settings Updated Successfully");
    }

    render() {
        return (
            <div className="d-flex">
                <Row className="mt-4 w-100 justify-content-between">
                    <Col xl={3} lg={4} md={4} sm={12} xs={12} className="ms-5">
                        <UserCard/>
                    </Col>
                    <Col xl={8} lg={8} md={8} sm={12} xs={12} className="border text-center">
                        <h2 className="mt-2 mb-2">General settings</h2>
                        <hr/>
                        <div className="row justify-content-around">
                            <div
                                className="col-12 col-sm-12 col-md-5 col-md-10 col-lg-10 col-xl-10 mb-3 bg-light border rounded">
                                <h3 className="mt-2 mb-2">Avatar settings</h3>
                                <hr className="mb-3"/>
                                { (this.state && this.state.avatarSettings)?
                                    <form onSubmit={this.handleSubmit}>
                                        <div className="row justify-content-center">
                                            <div
                                                className="input-group mb-3 col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
                                                <div className="input-group-append w-35">
                                                        <span
                                                            className="input-group-text justify-content-center input-group-text-input w-100">Resolution</span>
                                                </div>
                                                <select value={this.state.avatarSettings.resolution} onChange={this.handleChangeResolution}
                                                        className="form-control input_user">
                                                    <option value="720x720">720x720</option>
                                                    <option value="800x600">800x600</option>
                                                    <option value="1280x720">1280x720</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div className="row justify-content-center">
                                            <div
                                                className="input-group mb-3 col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
                                                <div className="input-group-append w-35">
                                                        <span
                                                            className="input-group-text justify-content-center input-group-text-input w-100">Language</span>
                                                </div>
                                                <select value={this.state.avatarSettings.language} onChange={this.handleChangeLanguage}
                                                        className="form-control input_user"
                                                >
                                                    <option value="en">English</option>
                                                    <option value="ro">Romanian</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div className="row justify-content-center">
                                            <div
                                                className="input-group mb-3 col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
                                                <div className="input-group-append w-35">
                                                        <span
                                                            className="input-group-text justify-content-center input-group-text-input w-100">Subtitles</span>
                                                </div>
                                                <select onChange={this.handleChangeEnableSubtitles}
                                                    value={this.state.avatarSettings.enableSubtitles ? "1" : "0"}
                                                        className="form-control input_user">
                                                    <option value="1">On</option>
                                                    <option value="0">Off</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div className="row justify-content-center">
                                            <div
                                                className="input-group mb-3 col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6">
                                                <div className="input-group-append w-35">
                                                        <span
                                                            className="input-group-text justify-content-center input-group-text-input w-100">Avatar</span>
                                                </div>
                                                <select onChange={this.handleChangeEnabled}
                                                        value={this.state.avatarSettings.enabled ? "1" : "0"}
                                                        className="form-control input_user"
                                                >
                                                    <option value="1">Enable</option>
                                                    <option value="0">Disable</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div className="d-flex justify-content-center mt-3 mb-3 register_container">
                                            <button type="submit" name="update_settings_button"
                                                    className="btn btn-primary">Update
                                            </button>
                                        </div>
                                    </form>: null
                                }
                            </div>

                        </div>
                        <hr/>
                    </Col>
                </Row>
            </div>
        );
    }
}
