const React = require('react');
import {App} from '../app'
import {KeyForm} from '../forms/keyform'
import {KeyList} from '../lists/keylist'
import {Router} from "react-router-dom";

export class Keys extends React.Component {

    constructor(props) {
        super(props);
        this.state = {keys: [], displayAddForm: false, refreshKeys: false};
        this.handleAddKey = this.handleAddKey.bind(this);
        this.handleKeySaved = this.handleKeySaved.bind(this);
    }


    handleKeySaved() {
        this.setState({displayAddForm: false});
        App.showMessage('success', 'Success', 'Key created')
        this.setState({refreshKeys: !this.state.refreshKeys});
    }
    handleAddKey() {
        this.setState({displayAddForm: true});
    }

    render() {
        return (
            <div>
                <KeyForm display={this.state.displayAddForm} onSaved={this.handleKeySaved}/>
                <button className="btn btn-success" onClick={this.handleAddKey}>Create new key</button>
                <KeyList refresh={this.state.refreshKeys}/>
            </div>
        )
    }
}