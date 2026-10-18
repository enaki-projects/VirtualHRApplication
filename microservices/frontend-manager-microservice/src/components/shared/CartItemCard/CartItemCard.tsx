import React from "react";
import {Card} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye, faTrash} from "@fortawesome/free-solid-svg-icons";
import {Item} from "../../../models/manager/item";
import {authenticationService} from "../../../services/AuthService";
import {EditCartItemModal} from "../../Modals/EditCartItemModal/EditCartItemModal";
import './CartItemCard.scss'
import {cartService} from "../../../services/CartService";
import {NotificationManager} from '../../../shared/notifications';

export default class CartItemCard extends React.Component<any, any> {
    currentItem: Item

    constructor(props) {
        super(props);
        this.state = {
            currentItem: props.item
        }
    }

    deleteItem(candidateId: number) {
        cartService.deleteItemFromCart(this.state.currentItem.cartId, candidateId).then((response) => {
            console.log(response);
            NotificationManager.info('Candidate Deleted');
            cartService.notifyOfCartFetch();
        }).catch(error => {
            console.log(error.response);
            NotificationManager.error(error.response.data.error || 'Candidate Deleting Failed', error.response.status);
        })
    }

    handleCallback = (childData) =>{
        console.log(childData);
        this.setState({currentItem: childData})
    }

    render() {
        const candidate = this.state.currentItem.candidate;
        const photoUrl = authenticationService.getUserPhotoUrl(candidate.hasImage, candidate.uid, candidate.gender);

        return (
            <Card>
                <Card.Header className="p-1">
                    <Card.Text className="text-center m-2">
                        <strong>{this.state.currentItem.candidate.firstname + ' ' + this.state.currentItem.candidate.lastname}</strong>
                    </Card.Text>
                </Card.Header>
                <Card.Body className="card-body p-1">
                    <div className="card-img-wrapper m-3 text-center">
                        <img src={photoUrl} alt="Photo Image"
                             className=" cart-picture p-0 m-0"/>
                        <ul className="list-inline member-icons animate text-center">
                            <li className="list-inline-item">
                                <a href={`/candidates/${candidate.uid}/from-cart/${this.state.currentItem.cartId}`} className="btn btn-success me-2"><FontAwesomeIcon icon={faEye}/></a>
                                <EditCartItemModal item={this.state.currentItem} cartItemCardUpdateCallback={this.handleCallback}/>
                                <button className="btn btn-danger" onClick={() => this.deleteItem(candidate.uid)}><FontAwesomeIcon icon={faTrash}/></button>
                            </li>
                        </ul>
                    </div>
                </Card.Body>
                <Card.Footer className="p-1">
                    <blockquote className="blockquote ms-4 me-4">
                        <Card.Text className="m-2">
                            <span><strong>Score: </strong>{this.state.currentItem.score}</span><br/>
                            <span><strong>Notes: </strong>{this.state.currentItem.notes || '-'}</span><br/>
                        </Card.Text>
                    </blockquote>
                </Card.Footer>
            </Card>
        )
    }
}
