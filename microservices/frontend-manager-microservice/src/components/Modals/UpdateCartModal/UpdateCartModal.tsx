import {Button, InputGroup, Modal, Row} from "react-bootstrap";
import React, {useCallback, useState} from "react";
import {cartService} from "../../../services/CartService";
import {NotificationManager} from '../../../shared/notifications';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEdit} from "@fortawesome/free-solid-svg-icons";

export function UpdateCartModal(props) {
    const isCancelled = React.useRef(false);

    React.useEffect(() => {
        return () => {
            isCancelled.current = true;
        };
    }, []);

    const [show, setShow] = useState(false);
    let [name, setName] = useState(props.cart.name);
    let [description, setDescription] = useState(props.cart.description);

    const handleClose = useCallback(() => setShow(false), []);
    const handleShow = useCallback(() => setShow(true), []);

    const handleChangeName = useCallback((event) => {
        if (!isCancelled.current) {
            setName(event.target.value);
        }
    }, []);

    const handleChangeDescription = useCallback((event) => {
        if (!isCancelled.current) {
            setDescription(event.target.value);
        }
    }, []);

    const handleSubmit = useCallback((event) => {
        const cart = {
            cartId: props.cart.cartId,
            name: name,
            description: description,
            creatorId: props.cart.creatorId
        }
        cartService.updateCart(cart).then((response) => {
            console.log(response);
            NotificationManager.info('Cart Updated Successfully');
            cartService.notifyOfCartFetch();
            handleClose();
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Cart Updating Failed', error.response.status);
        });
    }, [description, handleClose, name, props.cart.cartId, props.cart.creatorId]);

    return (
        <>
            <Button className="btn btn-info me-2" onClick={handleShow}><FontAwesomeIcon icon={faEdit}/></Button>

            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Update a cart</Modal.Title>
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
                        Update
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
