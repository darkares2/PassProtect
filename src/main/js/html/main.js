const React = require('react');
import {Route, Switch} from "react-router-dom";
import {Events} from '../views/events';
import {Keys} from '../views/keys';
import {Passwords} from '../views/passwords';
import {Login} from '../views/login'

export class Main extends React.Component {

    constructor(props) {
        super(props);
    }
    render() {
        return (
            <main className="container">
                <Switch>
                    <Route exact path='/' component={Events}/>
                    <Route path='/login' component={Login}/>
                    <Route path='/keys' component={Keys}/>
                    <Route path='/passwords' component={Passwords}/>
                    <Route component={Events} />
                </Switch>
            </main>
        )
    }
}