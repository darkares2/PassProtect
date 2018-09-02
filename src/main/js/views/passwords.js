import {App} from "../app";

const React = require('react');
import {PasswordForm} from "../forms/passwordform";
import {PasswordList} from "../lists/passwordlist";

export class Passwords extends React.Component {

    constructor(props) {
        super(props);
        this.state = {passwords: [], displayAddForm: false, refreshPasswords: false, safeMode: true};
        this.handleAddPassword = this.handleAddPassword.bind(this);
        this.handlePasswordSaved = this.handlePasswordSaved.bind(this);
        this.handleSafeModeToggle = this.handleSafeModeToggle.bind(this);
    }

    handlePasswordSaved() {
        this.setState({displayAddForm: false});
        App.showMessage('success', 'Success', 'Password created')
        this.setState({refreshPasswords: !this.state.refreshPasswords});
    }
    handleAddPassword() {
        this.setState({displayAddForm: true});
    }
    handleSafeModeToggle() {
        this.setState({safeMode: !this.state.safeMode});
    }

    render() {
        let addButton = null;
        if (!this.state.displayAddForm)
            addButton = (<button className="btn btn-success" onClick={this.handleAddPassword}>Create new password</button>);
        return (
            <div>
                <PasswordForm display={this.state.displayAddForm} onSaved={this.handlePasswordSaved}/>
                {addButton}
                <button className="offset-8 btn btn-danger" onClick={this.handleSafeModeToggle}>Safe mode toggle</button>
                <PasswordList refresh={this.state.refreshPasswords} safeMode={this.state.safeMode}/>
            </div>
        )
    }
}