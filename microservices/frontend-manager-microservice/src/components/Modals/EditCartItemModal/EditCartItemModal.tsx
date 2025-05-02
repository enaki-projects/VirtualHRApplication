import {Button, Col, InputGroup, Modal, Row} from "react-bootstrap";
import React, {useState} from "react";
import './EditCartItemModal.css'
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEdit} from "@fortawesome/free-solid-svg-icons";
import Rating from "react-rating";
import CandidateCardSimple from "../../shared/CandidateCardSimple/CandidateCardSimple";
import {Item} from "../../../models/manager/item";
import {cartService} from "../../../services/CartService";
import {NotificationManager} from 'react-notifications';

export function EditCartItemModal(props) {
    let item = props.item as Item;

    const [show, setShow] = useState(false);
    let [score, setScore] = useState(item.score);
    let [notes, setNotes] = useState(item.notes || '');

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleChangeScore = (event) => {
        setScore(event as number);
        item.score = event as number;
    }

    const handleChangeNotes = (event) => {
        setNotes(event.target.value);
        item.notes = event.target.value;
    }

    const handleSubmit = (event) => {
        console.log(item);
        cartService.updateCartItem(item).then(response => {
            NotificationManager.success('Candidate Updated Successfully');
            props.cartItemCardUpdateCallback(item);
            handleClose();
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Candidate Update Failed', error.response.status);
        })
    }

    return (
        <>
            <button className="btn btn-info mr-2" onClick={handleShow}><FontAwesomeIcon icon={faEdit}/></button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Edit Candidate Information</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row className="text-center justify-content-center">
                        <Col className="text-center mb-3 col-8 col-sm-8 col-md-8 col-lg-10 col-xl-10">
                            <CandidateCardSimple user={item.candidate}/>
                        </Col>
                    </Row>

                    <form>
                        <Row>
                            <Col className="text-center mb-3 col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                                <Rating
                                    initialRating={score}
                                    start={0}
                                    stop={10}
                                    step={1}
                                    fractions={4}
                                    onChange={handleChangeScore}
                                /><br/>
                                <strong>{score}</strong>
                            </Col>
                        </Row>
                        <Row>
                            <InputGroup className="mb-3 col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                                <InputGroup className="input-group mb-3">
                                    <span
                                        className="input-group-text input-group-text-area col-12 input-group-text-cst text-light">Notes: </span>
                                    <textarea aria-label="Type Description Here" className="form-control"
                                              value={item.notes} onChange={handleChangeNotes}/>
                                </InputGroup>
                            </InputGroup>
                        </Row>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                    <Button variant="primary" onClick={handleSubmit}>
                        Update
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
