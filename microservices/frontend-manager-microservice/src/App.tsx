import React from 'react';
import './App.css';
import {Subscription} from "rxjs";
import {authenticationService} from "./services/AuthService";
import {AuthenticatedUser} from "./models/authenticated-user";
import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
import {Home} from "./components/HomePage/Home";
import Login from "./components/Auth/Login/Login";
import Header from "./components/Nav/Header.jsx";
import {NotificationContainer} from './shared/notifications';
import {CandidatePage} from "./components/CandidatePage/CandidatePage";
import {CartPage} from "./components/CartPage/CartPage";
import CandidateCvView from './components/shared/CandidateCvView/CandidateCvView';
import CartView from "./components/shared/CartView/CartView";
import {SettingsPage} from "./components/SettingsPage/SettingsPage";


class App extends React.Component {
    private subscriptions: Subscription[];

    constructor(props: any) {
        super(props);
        this.subscriptions = [];
        this.state = {
            currentUser: null
        };
    }

    componentDidMount() {
        console.log("[ROOT] - componentDidMount")
        this.subscriptions.push(authenticationService.currentUser$.subscribe((x: AuthenticatedUser) => this.setState({currentUser: x})));
    }

    componentWillUnmount() {
        console.log("[ROOT] - componentWillUnmount")
        for (let i = 0; i < this.subscriptions.length; ++i) {
            this.subscriptions[i].unsubscribe();
        }
    }

    render() {
        const currentUser = authenticationService.getCurrentUserFromLocalStorage();
        let insideContainer;

        if (currentUser == null) {
            insideContainer =
                <Routes>
                    <Route path="/login" element={<Login/>}/>
                    <Route path="*" element={<Navigate to="/login" replace/>}/>
                </Routes>
        } else {
            insideContainer =
                <Routes>
                    <Route path="/" element={<Navigate to="/home" replace/>}/>
                    <Route path="/login" element={<Navigate to="/home" replace/>}/>
                    <Route path="/home/*" element={<Home/>}/>
                    <Route path="/candidates/:id/from-cart/:cartId" element={<CandidateCvView/>}/>
                    <Route path="/candidates/:id" element={<CandidateCvView/>}/>
                    <Route path="/candidates" element={<CandidatePage/>}/>
                    <Route path="/cart/:id" element={<CartView/>}/>
                    <Route path="/cart" element={<CartPage/>}/>
                    <Route path="/settings" element={<SettingsPage/>}/>
                </Routes>
        }

        return (
            <div className="wrapper">
                <BrowserRouter basename={import.meta.env.BASE_URL.replace(/\/$/, "")}>
                    {currentUser != null? <Header/>: null}
                    <NotificationContainer/>
                    {insideContainer}
                </BrowserRouter>
            </div>
        );
    }
}

export default App;
