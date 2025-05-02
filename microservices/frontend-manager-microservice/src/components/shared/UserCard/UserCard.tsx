import React from "react";
import {authenticationService} from "../../../services/AuthService";
import {AuthenticatedUser} from "../../../models/authenticated-user";
import {Card} from "react-bootstrap";
import moment from "moment";
import './UserCard.scss'


export default class UserCard extends React.Component<any, any> {
    currentUser: AuthenticatedUser

    constructor(props) {
        super(props);
        authenticationService.currentUser$.subscribe((user) => {
            this.currentUser = user;
        })
    }

    getAge(value: Date): number {
        const today = moment();
        const birthdate = moment(value);
        return today.diff(birthdate, 'years');
    }

    render() {
        const photoUrl = this.currentUser.urlImage;
        const age = this.getAge(this.currentUser.cv.birthdate);
        return (

            <Card>
                <Card.Header className="p-1">
                    <Card.Text className="text-center m-2">
                        {this.currentUser.cv.email}
                    </Card.Text>
                </Card.Header>
                <Card.Body className="card-body p-1">
                    <div className=" card-img-wrapper m-3 text-center">
                        <img src={photoUrl} alt="Photo Image"
                             className="img-fluid img-thumbnail card-img-top user-profile-picture p-0 m-0"/>
                        <ul className="list-inline member-icons animate text-center">
                            <li className="list-inline-item">

                            </li>
                        </ul>
                    </div>
                    <div className="ml-2">
                        <Card.Text className="m-2">
                            <strong>First Name: </strong>{this.currentUser.cv.firstname}
                        </Card.Text>
                        <Card.Text className="card-title m-2">
                            <strong>Last Name: </strong>{this.currentUser.cv.lastname}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Contact Email: </strong>{this.currentUser.cv.email}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Gender: </strong>{this.currentUser.cv.gender}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Birthdate: </strong>{this.currentUser.cv.birthdate}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Age: </strong>{age}
                        </Card.Text>
                        <Card.Text className=" m-2">
                            <strong>Motto: </strong>{this.currentUser.cv.motto}
                        </Card.Text>
                    </div>
                </Card.Body>
                <Card.Footer className="p-1">
                    <blockquote className="blockquote ml-4 mr-4">
                        <Card.Text className="mb-0">"{this.currentUser.cv.aboutMe}"</Card.Text>
                    </blockquote>
                </Card.Footer>
            </Card>
        )
    }
}
