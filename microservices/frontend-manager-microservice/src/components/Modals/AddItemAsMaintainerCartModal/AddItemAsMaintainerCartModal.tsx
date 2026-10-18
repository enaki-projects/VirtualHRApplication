import {Button, Col, Modal, Row} from "react-bootstrap";
import React, {useCallback, useEffect, useState} from "react";
import {cartService} from "../../../services/CartService";
import {NotificationManager} from '../../../shared/notifications';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faTrash, faUserPlus} from "@fortawesome/free-solid-svg-icons";
import ReactLoading from "../../../shared/Loading";

import CandidateCardSimple from "../../shared/CandidateCardSimple/CandidateCardSimple";
import {CartWithCandidate} from "../../../models/manager/cart-with-candidate";

export function AddItemAsMaintainerCartModal(props) {
    const isCancelled = React.useRef(false);

    const [show, setShow] = useState(false);
    const [loadingData, setLoadingData] = useState(false);
    const [carts, setCarts] = useState({});
    const [cartList, setCartList] = useState([]);

    useEffect(() => {
        if (!isCancelled.current){
        cartService.fetchCartsByUidWithMaintainer(props.maintainer.uid).then((response) => {
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

        })}
        return function cleanup() {
            isCancelled.current = true;
        }
    }, [props]);


    const handleClose = useCallback(() => setShow(false), []);
    const handleShow = useCallback(() => setShow(true), []);


    const addAsMaintainerToCart = useCallback((cartId: number, maintainerId: number) => {
        if (isCancelled.current) return;
        setLoadingData(true);
        cartService.addMaintainerToCart(cartId, maintainerId).then((response) => {
            console.log(response);
            let cartsCopy = {...carts};
            cartsCopy[cartId].belong = true;

            let localCartList = Object.keys(cartsCopy).map(function (key) {
                return cartsCopy[key]
            });

            setCarts(cartsCopy);
            setCartList(localCartList);

            NotificationManager.success('Maintainer Added Successfully');
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Maintainer Adding Failed', error.response.status);
        }).finally(() => {
            if (isCancelled.current) return;
            setLoadingData(false);
        });
    }, [carts]);

    const deleteMaintainerFromCart = useCallback((cartId: number, maintainerId: number) => {
        if (isCancelled.current) return;
        setLoadingData(true);
        cartService.deleteMaintainerFromCart(cartId, maintainerId).then((response) => {
            console.log(response);
            let cartsCopy = {...carts};
            cartsCopy[cartId].belong = false;

            let localCartList = Object.keys(cartsCopy).map(function (key) {
                return cartsCopy[key]
            });

            setCarts(cartsCopy);
            setCartList(localCartList);

            NotificationManager.info('Maintainer Deleted');
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Maintainer Deleting Failed', error.response.status);
        }).finally(() => {
            if (isCancelled.current) return;
            setLoadingData(false);
        });
    }, [carts]);

    const maintainerId = props.maintainer.uid;

    return (
        <>
            <Button className="btn btn-info me-2" onClick={handleShow}><FontAwesomeIcon icon={faUserPlus}/></Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Add Maintainer To Cart</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Row className="text-center justify-content-center">
                        <Col className="text-center mb-3 col-8 col-sm-8 col-md-8 col-lg-10 col-xl-10">
                            <CandidateCardSimple user={props.maintainer}/>
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
                                                    onClick={() => addAsMaintainerToCart(cart.cartId, maintainerId)}><FontAwesomeIcon
                                                icon={faUserPlus}/></Button>
                                            <Button disabled={!cart.belong || loadingData}
                                                    className="btn btn-danger m-1"
                                                    onClick={() => deleteMaintainerFromCart(cart.cartId, maintainerId)}><FontAwesomeIcon
                                                icon={faTrash}/></Button>
                                        </th>
                                    </tr>
                                )
                            ): null}
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
