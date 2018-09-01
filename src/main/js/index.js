import 'bootstrap';
const React = require('react');
const ReactDOM = require('react-dom');
import { BrowserRouter } from 'react-router-dom';
import {App} from "./app";






ReactDOM.render(
    <BrowserRouter>
        <App />
    </BrowserRouter>,
    document.getElementById('react')
)