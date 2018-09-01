const React = require('react');
const axios = require('axios');
import {Input} from '../formfields/input'
import {App} from '../app'

export class PasswordForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {passwords: [], displayAddForm: false};
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        const self = this;

        let name = this.refs.name.value();
        let description = this.refs.description.value();
        let password = this.refs.password.value();
        let keyId = this.refs.keyId.value();
        console.log("Name is " + name)

        axios.post('/api/v1/password/', { name: name, description: description, keyId: keyId, password: password}, {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.props.onSaved();
            })
            .catch(function (error) {
                App.handleErrorResponse(error, "Failed to save key")
            });

    }

    render() {
        if (this.props.display) {
            return (
                <form autocomplete="off">
                    <Input ref="name" id="name" autocomplete="false" type="text" label="Name" placeholder="Password name" help="Please enter the identifier of the password" value=""/>
                    <Input ref="password" id="passwordValue" type="password" label="Password" autocomplete="new-password" placeholder="Password" help="Please enter password" value=""/>
                    <Input ref="description" id="descriptionValue" type="text" label="Description" placeholder="Description" help="Please enter the description" value=""/>
                    <Input ref="keyId" id="keyid" type="text" label="Key" placeholder="Key" help="Please enter key id" value=""/>
                    <button className="btn btn-success" onClick={this.handleSubmit}>Save</button>
                </form>
            )
        } else
            return null;
    }
}