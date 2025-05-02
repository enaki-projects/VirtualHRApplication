import React from 'react';
import './App.css';
import {Subscription} from "rxjs";
import {authenticationService} from "./services/AuthService";
import {AuthenticatedUser} from "./models/authenticated-user";
import {BrowserRouter, Redirect, Route, Switch, useLocation} from "react-router-dom";
import {Home} from "./components/HomePage/Home";
import Login from "./components/Auth/Login/Login";
import Header from "./components/Nav/Header.jsx";
import {NotificationContainer} from 'react-notifications';
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
                <Switch>
                    <Route path="/login">
                        <Login/>
                    </Route>
                    <Route exact path="/">
                        <Redirect to="/login"/>
                    </Route>
                    <Route path="/">
                        <Redirect to="/login"/>
                    </Route>
                </Switch>
        } else {
            insideContainer =
                <Switch>
                    <Route exact path="/">
                        <Redirect to="/home"/>
                    </Route>
                    <Route exact path="/login">
                        <Redirect to="/home"/>
                    </Route>

                    <Route path="/home">
                        <Home/>
                    </Route>

                    <Route exact path="/candidates/:id/from-cart/:cartId" > <CandidateCvView/></Route>

                    <Route path="/candidates/:id" > <CandidateCvView/></Route>

                    <Route path="/candidates">
                        <CandidatePage/>
                    </Route>

                    <Route path="/cart/:id" > <CartView/></Route>
                    <Route path="/cart">
                        <CartPage/>
                    </Route>

                    <Route path="/settings">
                        <SettingsPage/>
                    </Route>

                </Switch>
        }

        return (
            <div className="wrapper">
                <BrowserRouter>
                    {currentUser != null? <Header/>: null}
                    <NotificationContainer/>
                    {insideContainer}
                </BrowserRouter>
            </div>
        );
    }
}

export default App;
