import React, {Component} from "react";
import {withRouter} from "react-router";
import {Button, Card, Col, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faArrowLeft, faEye, faTrash} from "@fortawesome/free-solid-svg-icons";
import {cartService} from "../../../services/CartService";
import {Cart} from "../../../models/manager/cart";
import ReactLoading from "react-loading";
import CartItemCard from "../CartItemCard/CartItemCard";
import "./CartView.scss"
import {NotificationManager} from 'react-notifications';
import {authenticationService} from "../../../services/AuthService";
import {AuthenticatedUser} from "../../../models/authenticated-user";

class CartView extends Component<any, any> {
    _isMounted = false;
    cart: Cart;
    currentUser: AuthenticatedUser;

    constructor(props) {
        super(props);
        this.state = {
            loadingData: true,
            notFound: false,
        };
        authenticationService.currentUser$.subscribe(currentUser => {
            this.currentUser = currentUser;
        })
        cartService.currentCartFetchNotifier$.subscribe(response => {
            const id = this.props.match.params.id;
            this.fetchCart(id);
        })
    }

    async componentDidMount() {
        this._isMounted = true;
        const id = this.props.match.params.id;
        this.fetchCart(id);
    }

    async fetchCart(id: number) {
        if (this._isMounted){
            this.setState({
                loadingData: true,
            });
        }
        cartService.fetchCartById(id).then(response => {
            this.cart = response.data;
            console.log(this.cart);
        }).catch(error => {
            NotificationManager.error(error.response.data.error || 'Cart Not Found', error.response.status);
            if (this._isMounted) {
                this.setState({notFound: true});
            }
        }).finally(() => {
            if (this._isMounted) {
                this.setState({loadingData: false});
            }
        })
    }

    async deleteMaintainer(maintainerId: number) {
        if (this._isMounted){
            this.setState({
                loadingData: true,
            });
        }

        cartService.deleteMaintainerFromCart(this.cart.cartId, maintainerId).then((response) => {
            console.log(response);
            NotificationManager.info('Maintainer Deleted');
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Maintainer Deleting Failed', error.response.status);
        }).finally(() => {
            const id = this.props.match.params.id;
            this.fetchCart(id);
        });
    }

    render() {
        console.log(this.currentUser)
        if (!this._isMounted || this.state.loadingData) {
            return <Row className="text-center justify-content-center align-items-center">
                <ReactLoading type="bars" color="purple" height={667} width={375}/>
            </Row>
        }
        return (
            <>
                <a href={"/cart"} className="btn btn-primary m-3"><FontAwesomeIcon icon={faArrowLeft}/> Back</a>

                {
                    (this.state.notFound)? <h1 className="text-center">Not Found ...</h1>:
                        <div className="border border-warning m-3">
                            <Card.Header className="p-1 bg-warning">
                                <Card.Text className="text-center m-2">
                                    <strong>Cart View</strong>
                                </Card.Text>
                            </Card.Header>
                            <Card.Body className="card-body p-1">
                                <Row>
                                    <Col xl={8} lg={8} md={8} sm={12} xs={12}>
                                        <Row>
                                            {this.cart.items.length > 0 ?
                                                this.cart.items.map(item =>
                                                    <Col  key={item.cartItemId} xl={3} lg={4} md={4} sm={12} xs={12}>
                                                        <CartItemCard item={item}/>
                                                    </Col>
                                                ) :
                                                <Col className="text-center mt-2" xl={12} lg={12} md={12} sm={12} xs={12}>
                                                    <h4>No Selected Candidates ...</h4>
                                                </Col>
                                            }
                                        </Row>
                                    </Col>
                                    <Col className="p-0" xl={4} lg={4} md={4} sm={12} xs={12}>
                                        <Card className="w-95 h-100">
                                            <Card.Header className="bg-white">
                                                <span><strong>Cart Name: </strong>{this.cart.name}</span><br/>
                                                <span><strong>No. Maintainers: </strong>{this.cart.maintainers.length}</span><br/>
                                                <span><strong>No. Candidates: </strong>{this.cart.items.length}</span><br/>
                                                <span><strong>Description: </strong>"{this.cart.description}"</span><br/>
                                            </Card.Header>
                                            <Card.Body>
                                                <table className="table table-striped text-center">
                                                    <thead className="bg-info text-light">
                                                    <tr>
                                                        <th className="align-middle" scope="col">Maintainer</th>
                                                        <th className="align-middle" scope="col">Role</th>
                                                        <th className="align-middle" scope="col">Actions</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    {this.cart.maintainers.map(maintainer => (
                                                        <tr key={maintainer.cartMaintainerId}>
                                                            <th className="align-middle" scope="col">{maintainer.maintainer.firstname + ' ' + maintainer.maintainer.lastname}</th>
                                                            <th className="align-middle" scope="col">{maintainer.role}</th>
                                                            <th className="align-middle" scope="col">
                                                                <a href={`/candidates/${maintainer.maintainerId}/from-cart/${maintainer.cartId}`} className="btn btn-success mr-2"><FontAwesomeIcon icon={faEye}/></a>
                                                                {
                                                                    ((maintainer.role === "creator") || (maintainer.maintainerId === this.currentUser.cv.uid)) ? null:
                                                                        <Button className="btn btn-danger m-1" onClick={() => this.deleteMaintainer(maintainer.maintainerId)}><FontAwesomeIcon icon={faTrash}/></Button>
                                                                }
                                                            </th>
                                                        </tr>
                                                    ))}
                                                    </tbody>
                                                </table>
                                            </Card.Body>
                                        </Card>
                                    </Col>
                                </Row>
                            </Card.Body>
                        </div>
                }
            </>
        )
    }
}

export default withRouter(CartView)
