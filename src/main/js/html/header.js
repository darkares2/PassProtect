const React = require('react');
import {NavLink} from "react-router-dom";

export class Header extends React.Component {

    constructor(props) {
        super(props);
    }
    render() {
        return (
            <header className="container-fluid">
                <nav className="navbar navbar-expand-sm">
                    <NavLink className="navbar-brand" to='/'>PassProtect</NavLink>
                    <ul className="navbar-nav">
                        <li className="nav-item"><NavLink className="nav-link" to='/'>Events</NavLink></li>
                        <li className="nav-item"><NavLink className="nav-link" to='/keys'>Keys</NavLink></li>
                        <li className="nav-item"><NavLink className="nav-link" to='/passwords'>Passwords</NavLink></li>
                    </ul>
                </nav>
            </header>
        )
    }
}