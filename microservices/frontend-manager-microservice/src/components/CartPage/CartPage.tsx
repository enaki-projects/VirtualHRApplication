import React from "react";
import {AuthenticatedUser} from "../../models/authenticated-user";
import {authenticationService} from "../../services/AuthService";
import {avatarService} from "../../services/AvatarService";
import {environment} from "../../environments/environment";
import {Col, Row} from "react-bootstrap";
import AvatarVideoPlayer from "../shared/Avatar/AvatarVideoPlayer";
import {AddCartModal} from "../Modals/AddCartModal/AddCartModal";
import {Cart} from "../../models/manager/cart";
import CartCard from "../shared/CartCard/CartCard";
import {cartService} from "../../services/CartService";
import ReactLoading from "react-loading";


export class CartPage extends React.Component<any, any> {
    currentUser: AuthenticatedUser;
    carts: Cart[];
    _isMounted = false;

    constructor(props) {
        super(props);
        authenticationService.currentUser$.subscribe((user) => {
            this.currentUser = user;
        })
        cartService.currentCartFetchNotifier$.subscribe((update: boolean) => {
            if (update && this._isMounted) {
                this.setState({loadingData: true});
                this.fetchCarts().then(r => {
                })
            }
        })
        this.state = {
            loadingData: true,
        };
        this.carts = [];
    }

    async componentDidMount() {
        this._isMounted = true;
        await this.fetchCarts();
        await avatarService.updateAvatarVideo(environment.avatarScenarios.carts);
    }

    async fetchCarts() {
        if (!this._isMounted) return;
        cartService.fetchCartsByUid().then(response => {
            if (response.data) {
                this.carts = response.data;
            }
        }).finally(() => {
            this.setState({loadingData: false});
        })
    }

    render() {

        return <div className="d-flex">
            <Row className="mt-4 w-100 justify-content-between">
                <Col xl={8} lg={8} md={8} sm={12} xs={12} className="text-center">
                    <Row className="align-content-end text-right">
                        <Col className="justify-content-end">
                            <AddCartModal/>
                        </Col>
                    </Row>
                    {
                        (!this.state || this.state.loadingData)?
                            <Row className="text-center justify-content-center align-items-center">
                                <ReactLoading type="bars" color="purple" height={667} width={375}/>
                            </Row>:
                            <Row className="mt-4 w-100 ml-2">
                                {
                                    this.carts.length > 0 ?
                                        this.carts.map(item =>
                                            <Col className="mb-4" key={item.cartId} xl={3} lg={3} md={4} sm={12} xs={12}>
                                                <CartCard cart={item}/>
                                            </Col>)
                                        :
                                        <Col className="text-center mt-2" xl={12} lg={12} md={12} sm={12} xs={12}>
                                            <h4>No Created Carts ...</h4>
                                        </Col>
                                }
                            </Row>
                    }
                </Col>
                <Col xl={4} lg={4} md={4} sm={12} xs={12}>
                    <AvatarVideoPlayer/>
                </Col>
            </Row>
        </div>;
    }
}
