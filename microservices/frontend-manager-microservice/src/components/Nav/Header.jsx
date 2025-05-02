import './Header.css';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome'
import {NavLink} from 'react-router-dom'
import {faCogs, faHome, faShoppingCart, faSignOutAlt, faUsers} from '@fortawesome/free-solid-svg-icons'
import {authenticationService} from "../../services/AuthService";
import React from 'react';
import {Nav} from 'react-bootstrap';

export default function Header() {
    return (
        <Nav fill className="bg-dark" variant="tabs">
            <NavLink to="/home" activeClassName="selected" className="navElement nav-item"
                     href="/home"><FontAwesomeIcon icon={faHome}/> Home</NavLink>
            <NavLink to="/candidates" activeClassName="selected" className="navElement nav-item"
                     href="/candidates"><FontAwesomeIcon icon={faUsers}/> Candidates</NavLink>
            <NavLink to="/cart" activeClassName="selected" className="navElement nav-item" href="/cart"><FontAwesomeIcon
                icon={faShoppingCart}/> Cart</NavLink>
            <NavLink to="/settings" activeClassName="selected" className="navElement nav-item" href="/settings"><FontAwesomeIcon
                icon={faCogs}/> Settings</NavLink>
            <NavLink activeClassName="selected" className="navElement nav-item"
                     onClick={authenticationService.logout} to="/log-out"><FontAwesomeIcon icon={faSignOutAlt}/> Log Out</NavLink>

        </Nav>
    )
}
