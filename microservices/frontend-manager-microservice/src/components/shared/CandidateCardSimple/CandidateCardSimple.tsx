import React from "react";
import {Card} from "react-bootstrap";
import './CandidateCardSimple.scss'
import {CandidateCv} from "../../../models/cv/candidate-cv";
import {authenticationService} from "../../../services/AuthService";
import {userService} from "../../../services/UserService";


export default class CandidateCardSimple extends React.Component<any, any> {
    currentUserCv: CandidateCv

    constructor(props) {
        super(props);
        this.currentUserCv = this.props.user;
    }

    render() {
        const photoUrl = authenticationService.getUserPhotoUrl(this.currentUserCv.hasImage, this.currentUserCv.uid, this.currentUserCv.gender);
        const age = userService.getAge(this.currentUserCv.birthdate);
        return (
            <Card>
                <Card.Header className="p-1">
                    <Card.Text className="text-center m-2">
                        {this.currentUserCv.email}
                    </Card.Text>
                </Card.Header>
                <Card.Body className="card-body p-1">
                    <div className=" card-img-wrapper m-3 text-center">
                        <img src={photoUrl} alt="Candidate Image"
                             className="img-fluid img-thumbnail card-img-top candidate-profile-picture p-0 m-0"/>
                    </div>
                    <div className="ms-2">
                        <Card.Text className="m-2">
                            <strong>First Name: </strong>{this.currentUserCv.firstname}
                        </Card.Text>
                        <Card.Text className="card-title m-2">
                            <strong>Last Name: </strong>{this.currentUserCv.lastname}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Gender: </strong>{this.currentUserCv.gender}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Age: </strong>{age}
                        </Card.Text>
                    </div>
                </Card.Body>
                <Card.Footer className="p-1">
                    <blockquote className="ms-4 me-4">
                        <Card.Text className="mb-0">"{this.currentUserCv.aboutMe}"</Card.Text>
                    </blockquote>
                </Card.Footer>
            </Card>
        )
    }
}
