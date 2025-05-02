import React from "react";
import {Card} from "react-bootstrap";
import './CartCard.scss'
import {Cart} from "../../../models/manager/cart";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faTrash} from "@fortawesome/free-solid-svg-icons";
import {cartService} from "../../../services/CartService";
import {NotificationManager} from 'react-notifications';
import {UpdateCartModal} from "../../Modals/UpdateCartModal/UpdateCartModal";
import {myUtils} from "../../../shared/utils";


export default class CartCard extends React.Component<any, any> {
    currentCart: Cart

    constructor(props) {
        super(props);
        this.currentCart = props.cart;
        this.deleteCart = this.deleteCart.bind(this);
    }

    deleteCart(cartId: number){
        cartService.deleteCart(cartId).then(response => {
            NotificationManager.success('Cart Deleted Successfully');
            cartService.notifyOfCartFetch();
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Cart Deletion Failed', error.response.status);
        });
    }

    render() {
        const imgLink = `https://dummyimage.com/512x512/000/${myUtils.getRandomColor()}.png&text=${this.currentCart.name}`
        return (
            <Card>
                <Card.Header className="p-1">
                    <Card.Text className="text-center m-2">
                        <strong>{this.currentCart.name}</strong>
                    </Card.Text>
                </Card.Header>
                <Card.Body className="card-body p-1">
                    <div className="card-img-wrapper m-3 text-center">
                        <img src={imgLink} alt="Photo Image"
                             className="img-fluid img-thumbnail card-img-top cart-picture p-0 m-0"/>
                        <ul className="list-inline member-icons animate text-center">
                            <li className="list-inline-item">
                                <a href={"/cart/" + this.currentCart.cartId} className="btn btn-success mr-2"><FontAwesomeIcon icon={faEye}/></a>
                                <UpdateCartModal cart={this.currentCart}/>
                                <button className="btn btn-danger"><FontAwesomeIcon icon={faTrash} onClick={() => this.deleteCart(this.currentCart.cartId)}/></button>
                            </li>
                        </ul>
                    </div>
                </Card.Body>
                <Card.Footer className="p-1">
                    <blockquote className="blockquote ml-4 mr-4">
                        <Card.Text className="m-2">
                            <strong>Description: </strong>{myUtils.getShortDescription(this.currentCart.description)}
                        </Card.Text>
                    </blockquote>
                </Card.Footer>
            </Card>
        )
    }
}
