import React from "react";
import {Col, Row} from "react-bootstrap";
import AvatarVideoPlayer from "../shared/Avatar/AvatarVideoPlayer";
import {avatarService} from "../../services/AvatarService";
import {environment} from "../../environments/environment";
import UserCard from "../shared/UserCard/UserCard";
import {AuthenticatedUser} from "../../models/authenticated-user";
import {authenticationService} from "../../services/AuthService";


export class Home extends React.Component<any, any>{
    currentUser: AuthenticatedUser

    constructor(props) {
        super(props);
        authenticationService.currentUser$.subscribe((user) => {
            this.currentUser = user;
        })
    }

    componentDidMount() {
        avatarService.updateAvatarVideo(environment.avatarScenarios.home);
    }

    render() {
        return (
            <div className="d-flex">
                <Row className="mt-4 w-100 justify-content-between">
                    <Col xl={3} lg={4} md={4} sm={6} xs={6} className="ml-5">
                        <UserCard/>
                    </Col>
                    <Col xl={4} lg={4} md={3} sm={5} xs={5} className="text-center">
                        <h1>Hello {this.currentUser.cv.firstname}</h1>
                    </Col>
                    <Col xl={4} lg={4} md={4} sm={12} xs={12}>
                        <AvatarVideoPlayer/>
                    </Col>
                </Row>
            </div>
        );
    }
}
