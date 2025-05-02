import {authenticationService} from "./AuthService";
import axios from "axios";
import {AuthenticatedUser} from "../models/authenticated-user";
import {Item} from "../models/manager/item";
import {BehaviorSubject} from "rxjs";

import {config} from "../environments/config";

const environment = config.environment;

let currentUser: AuthenticatedUser;

let currentCartFetchNotifierSource: BehaviorSubject<boolean> = new BehaviorSubject(false);

authenticationService.currentUser$.subscribe((user) => {
    currentUser = user;
})

function addCart(cart): Promise<any>{
    let cartToCreate = {
        name: cart.name,
        description: cart.description,
        creatorId: currentUser.uid
    }
    return axios.post(`${environment.adapterApiUrl}manager/carts`, cartToCreate, {headers: authenticationService.authHeader()});
}

function updateCart(cart): Promise<any>{
    console.log(cart);
    return axios.put(`${environment.adapterApiUrl}manager/carts/${cart.cartId}`, cart, {headers: authenticationService.authHeader()});
}

function updateCartItem(cartItem: Item): Promise<any>{
    return axios.put(`${environment.adapterApiUrl}manager/cart-items/${cartItem.cartItemId}`, cartItem, {headers: authenticationService.authHeader()});
}

function fetchCartsByUid(): Promise<any>{
    return axios.get(`${environment.adapterApiUrl}manager/${currentUser.uid}/carts`, {headers: authenticationService.authHeader()});
}

function fetchCartsByUidWithCandidate(candidateId: number): Promise<any>{
    return axios.get(`${environment.adapterApiUrl}manager/${currentUser.uid}/carts/with-candidate/${candidateId}`, {headers: authenticationService.authHeader()});
}

function fetchCartsByUidWithMaintainer(maintainerId: number): Promise<any>{
    return axios.get(`${environment.adapterApiUrl}manager/${currentUser.uid}/carts/with-maintainer/${maintainerId}`, {headers: authenticationService.authHeader()});
}

function fetchCartById(id: number): Promise<any>{
    return axios.get(`${environment.adapterApiUrl}manager/carts/${id}`, {headers: authenticationService.authHeader()});
}

function notifyOfCartFetch(): void {
    currentCartFetchNotifierSource.next(true);
}

function addItemToCart(cartId: number, candidateId: number): Promise<any>{
    const data = {
        "cartId": cartId,
        "candidateId": candidateId,
    }
    return axios.post(`${environment.adapterApiUrl}manager/cart-items`, data, {headers: authenticationService.authHeader()});
}

function addMaintainerToCart(cartId: number, maintainerId: number): Promise<any>{
    const data = {
        "cartId": cartId,
        "maintainerId": maintainerId,
        "role": "editor",
    }
    return axios.post(`${environment.adapterApiUrl}manager/cart-maintainers`, data, {headers: authenticationService.authHeader()});
}

function deleteItemFromCart(cartId: number, candidateId: number): Promise<any>{
    return axios.delete(`${environment.adapterApiUrl}manager/carts/${cartId}/candidate/${candidateId}`, {headers: authenticationService.authHeader()});
}

function deleteMaintainerFromCart(cartId: number, maintainerId: number): Promise<any>{
    return axios.delete(`${environment.adapterApiUrl}manager/carts/${cartId}/maintainer/${maintainerId}`, {headers: authenticationService.authHeader()});
}

function deleteCart(cartId: number): Promise<any>{
    return axios.delete(`${environment.adapterApiUrl}manager/carts/${cartId}`, {headers: authenticationService.authHeader()});
}

export const cartService: any = {
    currentCartFetchNotifier$: currentCartFetchNotifierSource.asObservable(),
    get currentNotificationValue () { return currentCartFetchNotifierSource.value },
    addCart,
    updateCart,
    fetchCartsByUid,
    fetchCartsByUidWithCandidate,
    fetchCartsByUidWithMaintainer,
    updateCartItem,
    fetchCartById,
    notifyOfCartFetch,

    addItemToCart,
    addMaintainerToCart,

    deleteItemFromCart,
    deleteMaintainerFromCart,
    deleteCart
};
