import React from "react";
import {Card} from "react-bootstrap";
import './CandidateCard.scss'
import {CandidateCv} from "../../../models/cv/candidate-cv";
import {authenticationService} from "../../../services/AuthService";
import {faEye} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {userService} from "../../../services/UserService";
import {AddItemToCartModal} from "../../Modals/AddItemToCartModal/AddItemToCartModal";
import {AddItemAsMaintainerCartModal} from "../../Modals/AddItemAsMaintainerCartModal/AddItemAsMaintainerCartModal";


export default class CandidateCard extends React.Component<any, any> {
    currentUserCv: CandidateCv

    constructor(props) {
        super(props);
        this.currentUserCv = this.props.user;
    }

    render() {
        const photoUrl = authenticationService.getUserPhotoUrl(this.currentUserCv.hasImage, this.currentUserCv.uid, this.currentUserCv.gender);
        const age = userService.getAge(this.currentUserCv.birthdate);
        return (
            <Card >
                <Card.Header className="p-1">
                    <Card.Text className="text-center m-2">
                        {this.currentUserCv.email}
                    </Card.Text>
                </Card.Header>
                <Card.Body className="card-body p-1">
                    <div className=" card-img-wrapper m-3 text-center">
                        <img src={photoUrl} alt="Photo Image"
                             className="img-fluid img-thumbnail card-img-top candidate-profile-picture p-0 m-0"/>
                        <ul className="list-inline member-icons animate text-center">
                            <li className="list-inline-item">
                                <AddItemToCartModal candidate={this.currentUserCv}/>
                                <a href={"/candidates/" + this.currentUserCv.uid} className="btn btn-success mr-2"><FontAwesomeIcon icon={faEye}/></a>
                                <AddItemAsMaintainerCartModal maintainer={this.currentUserCv}/>
                            </li>
                        </ul>
                    </div>
                    <div className="ml-2">
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
                    <blockquote className="ml-4 mr-4">
                        <Card.Text className="mb-0">"{this.currentUserCv.aboutMe}"</Card.Text>
                    </blockquote>
                </Card.Footer>
            </Card>
        )
    }
}
