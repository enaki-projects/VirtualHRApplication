import {Button, Col, Modal, Row} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import {cartService} from "../../../services/CartService";
import {NotificationManager} from '../../../shared/notifications';
import CandidateCardSimple from "../../shared/CandidateCardSimple/CandidateCardSimple";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTrash, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import {CartWithCandidate} from "../../../models/manager/cart-with-candidate";
import ReactLoading from "../../../shared/Loading";


export function AddItemToCartModal(props) {
    const isCancelled = React.useRef(false);

    const [show, setShow] = useState(false);
    const [loadingData, setLoadingData] = useState(false);
    const [carts, setCarts] = useState({});
    const [cartList, setCartList] = useState([]);

    useEffect(() => {

        if (!isCancelled.current){
            cartService.fetchCartsByUidWithCandidate(props.candidate.uid).then((response) => {
                let localCarts = response.data as CartWithCandidate[];
                let cartDict = {}
                for (let idx = 0; idx < localCarts.length; ++idx) {
                    cartDict[localCarts[idx].cartId] = localCarts[idx];
                }
                let localCartList = Object.keys(cartDict).map(function (key) {
                    return cartDict[key]
                });

                setCarts(cartDict);
                setCartList(localCartList);

            });
        }
        return function cleanup() {
            isCancelled.current = true;
        }
    }, [props]);


    const handleClose = useCallback(() => setShow(false), []);
    const handleShow = useCallback(() => setShow(true), []);


    const addToCart = useCallback((cartId: number, candidateId: number) => {
        setLoadingData(true);
        cartService.addItemToCart(cartId, candidateId).then((response) => {
            console.log(response);
            let cartsCopy = {...carts};
            cartsCopy[cartId].belong = true;

            let localCartList = Object.keys(cartsCopy).map(function (key) {
                return cartsCopy[key]
            });

            setCarts(cartsCopy);
            setCartList(localCartList);

            NotificationManager.success('Candidate Added Successfully');
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Candidate Adding Failed', error.response.status);
        }).finally(() => {
            setLoadingData(false);
        })
    }, [carts]);

    const deleteFromCart = useCallback((cartId: number, candidateId: number) => {
        setLoadingData(true);
        cartService.deleteItemFromCart(cartId, candidateId).then((response) => {
            console.log(response);
            let cartsCopy = {...carts};
            cartsCopy[cartId].belong = false;

            let localCartList = Object.keys(cartsCopy).map(function (key) {
                return cartsCopy[key]
            });

            setCarts(cartsCopy);
            setCartList(localCartList);

            NotificationManager.info('Candidate Deleted');
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Candidate Deleting Failed', error.response.status);
        }).finally(() => {
            setLoadingData(false);
        });
    }, [carts]);


    const candidateId = props.candidate.uid;

    return (
        <>
            <Button className="btn btn-warning me-2" onClick={handleShow}><FontAwesomeIcon icon={faUserPlus}/></Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Candidate To Cart</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row className="text-center justify-content-center">
                        <Col className="text-center mb-3 col-8 col-sm-8 col-md-8 col-lg-10 col-xl-10">
                            <CandidateCardSimple user={props.candidate}/>
                        </Col>
                    </Row>

                    {
                        loadingData ?
                            <Row className="text-center justify-content-center">
                                <ReactLoading type="bars" color="purple"/>
                            </Row> : null
                    }

                    <div>
                        <table className="table table-striped text-center">
                            <thead>
                            <tr>
                                <th className="align-middle" scope="col">Cart Name</th>
                                <th className="align-middle" scope="col">Actions</th>
                            </tr>
                            </thead>
                            <tbody>
                            {cartList? cartList.map(cart =>
                                (
                                    <tr key={cart.cartId}>
                                        <th className="align-middle text-center" scope="col">{cart.name}</th>

                                        <th className="align-middle text-center" scope="col">
                                            <Button disabled={cart.belong || loadingData}
                                                    className="btn btn-success m-1"
                                                    onClick={() => addToCart(cart.cartId, candidateId)}><FontAwesomeIcon
                                                icon={faUserPlus}/></Button>
                                            <Button disabled={!cart.belong || loadingData}
                                                    className="btn btn-danger m-1"
                                                    onClick={() => deleteFromCart(cart.cartId, candidateId)}><FontAwesomeIcon
                                                icon={faTrash}/></Button>
                                        </th>
                                    </tr>
                                )
                            ):null}
                            </tbody>
                        </table>
                    </div>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleClose}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
}
