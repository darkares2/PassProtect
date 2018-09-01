import {App} from "../app";

const React = require('react');
import {PasswordForm} from "../forms/passwordform";
import {PasswordList} from "../lists/passwordlist";

export class Passwords extends React.Component {

    constructor(props) {
        super(props);
        this.state = {passwords: [], displayAddForm: false, refreshPasswords: false};
        this.handleAddPassword = this.handleAddPassword.bind(this);
        this.handlePasswordSaved = this.handlePasswordSaved.bind(this);
    }

    handlePasswordSaved() {
        this.setState({displayAddForm: false});
        App.showMessage('success', 'Success', 'Password created')
        this.setState({refreshPasswords: !this.state.refreshPasswords});
    }
    handleAddPassword() {
        this.setState({displayAddForm: true});
    }

    render() {
        return (
            <div>
                <PasswordForm display={this.state.displayAddForm} onSaved={this.handlePasswordSaved}/>
                <button className="btn btn-success" onClick={this.handleAddPassword}>Create new password</button>
                <PasswordList refresh={this.state.refreshPasswords}/>
            </div>
        )
    }
}