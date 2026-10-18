import {Button, InputGroup, Modal, Row} from "react-bootstrap";
import React, {useState} from "react";
import {cartService} from "../../../services/CartService";
import {NotificationManager} from '../../../shared/notifications';
import './AddCartModal.css'

export function AddCartModal() {
    const [show, setShow] = useState(false);
    let [name, setName] = useState('');
    let [description, setDescription] = useState('');

    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    const handleChangeName = (event) => {
        setName(event.target.value);
    }

    const handleChangeDescription = (event) => {
        setDescription(event.target.value);
    }

    const handleSubmit = (event) => {
        const cart = {
            name: name,
            description: description,
        }
        cartService.addCart(cart).then((response) => {
            console.log(response);
            NotificationManager.success('Cart Added Successfully');
            cartService.notifyOfCartFetch();
            handleClose();
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Cart Adding Failed', error.response.status);
        });
    }

    return (
        <>
            <Button variant="primary" onClick={handleShow}>
                Add Cart
            </Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add a cart</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <form>
                        <Row>
                            <InputGroup className="mb-3 col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                                <div className="input-group-text input-group-text-cst mb-3">Name</div>
                                <input type="text" value={name} className="form-control" onChange={handleChangeName}/>
                            </InputGroup>
                        </Row>
                        <Row>
                            <InputGroup className="mb-3 col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                                <InputGroup className="input-group mb-3">
                                    <span
                                        className="input-group-text input-group-text-area col-12 input-group-text-cst text-light">Description: </span>
                                    <textarea aria-label="Type Description Here" className="form-control"
                                              value={description} onChange={handleChangeDescription}/>
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
                        Add
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
