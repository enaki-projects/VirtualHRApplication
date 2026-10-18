import React, {useState} from 'react';
import './Login.css';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'

import {faLock, faUser} from '@fortawesome/free-solid-svg-icons'
import {LoginUser} from "../../../models/login-user";
import {Col, Container, FormControl, InputGroup, Row} from "react-bootstrap";
import {authenticationService} from "../../../services/AuthService";
import {NotificationManager} from '../../../shared/notifications';
import {config} from "../../../environments/config";


export default function Login() {
    const [email, setEmail] = useState();
    const [password, setPassword] = useState();

    const handleSubmit = async e => {
        e.preventDefault();
        const credentials: LoginUser = {
            "email": email,
            "password": password
        }
        authenticationService.login(credentials)
            .then((result: any) => {
                NotificationManager.success('Login Successful');
            })
            .catch(error => {
                console.log(error)
                const msg = (error.response.data.message !== undefined)? error.response.data.message: error.response.data.details;
                NotificationManager.error(msg, error.response.status);
            });
    }

    const userFrontendUrl = config.environment.userFrontendUrl;

    return (
        <div className="background">
            <div className="login-page-container">
                <Row className="login-component m-0 justify-content-center">
                    <Col xs={3} className="user_card my-auto">
                        <Container className="d-flex justify-content-center">
                            <div className="brand_logo_container">
                                <img src={import.meta.env.BASE_URL + "assets/logo.png"} className="brand_logo"
                                     alt="Logo"/>
                            </div>
                        </Container>
                        <Container className="d-flex justify-content-center pt-0 mt-4 w-75 ">
                            <form onSubmit={handleSubmit}>
                                <InputGroup className="mb-3">
                                        <InputGroup.Text> <FontAwesomeIcon icon={faUser}/></InputGroup.Text>
                                    <FormControl
                                        placeholder="Email"
                                        aria-label="Email"
                                        onChange={e => {
                                            // @ts-ignore
                                            setEmail(e.target.value);
                                        }}
                                    />
                                </InputGroup>
                                <InputGroup className="mb-3">
                                        <InputGroup.Text> <FontAwesomeIcon icon={faLock}/></InputGroup.Text>
                                    <FormControl
                                        placeholder="Password"
                                        aria-label="Password"
                                        type="password"
                                        onChange={e => {
                                            // @ts-ignore
                                            setPassword(e.target.value);
                                        }}
                                    />
                                </InputGroup>
                                <div className="d-flex justify-content-center mt-3 login_container">
                                    <button type="submit" name="button" className="btn login_btn">Login</button>
                                </div>
                            </form>

                        </Container>

                        <div className="mt-4">
                            <div className="d-flex justify-content-center links">
                                Don't have an account? <a href={userFrontendUrl + "auth/register"} className="ms-2">Sign
                                Up</a>
                            </div>
                        </div>
                        <div className="mt-1 mb-4">
                            <div className="d-flex justify-content-center links">
                                Forget Password? <a href={userFrontendUrl + "auth/recover"} className="ms-2">Recover</a>
                            </div>
                        </div>
                    </Col>
                </Row>
            </div>
        </div>
    )
}
