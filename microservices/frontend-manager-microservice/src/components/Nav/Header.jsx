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
            <NavLink to="/home" className={({isActive}) => "navElement nav-item" + (isActive ? " selected" : "")}><FontAwesomeIcon icon={faHome}/> Home</NavLink>
            <NavLink to="/candidates" className={({isActive}) => "navElement nav-item" + (isActive ? " selected" : "")}><FontAwesomeIcon icon={faUsers}/> Candidates</NavLink>
            <NavLink to="/cart" className={({isActive}) => "navElement nav-item" + (isActive ? " selected" : "")}><FontAwesomeIcon
                icon={faShoppingCart}/> Cart</NavLink>
            <NavLink to="/settings" className={({isActive}) => "navElement nav-item" + (isActive ? " selected" : "")}><FontAwesomeIcon
                icon={faCogs}/> Settings</NavLink>
            <NavLink className={({isActive}) => "navElement nav-item" + (isActive ? " selected" : "")}
                     onClick={authenticationService.logout} to="/log-out"><FontAwesomeIcon icon={faSignOutAlt}/> Log Out</NavLink>

        </Nav>
    )
}
