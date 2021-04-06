import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Fridge from "./Fridge";
import Recipe from "./Recipe";

const rootElement = document.getElementById('root');
ReactDOM.render(
<BrowserRouter>
    <Switch>
        <Route exact path="/" component={Fridge} />
        <Route path="/Recipe" component={Recipe} />
    </Switch>
</BrowserRouter>,
    rootElement
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
