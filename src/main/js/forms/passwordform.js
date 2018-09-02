import {Password} from "../entities/password";

const React = require('react');
const axios = require('axios');
import {Input} from '../formfields/input'
import {Select} from '../formfields/select'
import {App} from '../app'

export class PasswordForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {keys: []};
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.loadKeys();
        this.generatePassword();
    }

    componentDidUpdate(prevProps) {
        // Typical usage (don't forget to compare props):
        if (this.props.display !== prevProps.display) {
            this.generatePassword();
        }
    }

    loadKeys() {
        const self = this;
        axios.get('/api/v1/key/all', {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({keys: response.data});
            })
            .catch(function (error) {
                console.log(error);
                App.handleErrorResponse(error, "Failed to load keys")
            });
    }

    generatePassword() {
        const self = this;
        axios.get('/api/v1/password/generate', {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({password: response.data});
            })
            .catch(function (error) {
                console.log(error);
                App.handleErrorResponse(error, "Failed to load keys")
            });
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
            let keys = [];
            this.state.keys.map(key =>
                keys.push({value: key.id, text: key.name})
            );
            let formType = "row";

            return (
                <form autoComplete="off">
                    <Input formType={formType} ref="name" id="name" autocomplete="false" type="text" label="Name" placeholder="Password name" help="Please enter the identifier of the password" value=""/>
                    <Input formType={formType} ref="password" id="passwordValue" type="text" label="Password" autocomplete="new-password" placeholder="Password" help="Please enter password" value={this.state.password}/>
                    <Input formType={formType} ref="description" id="descriptionValue" type="text" label="Description" placeholder="Description" help="Please enter the description" value=""/>
                    <Select formType={formType} ref="keyId" id="keyid" type="text" label="Key" placeholder="Key" help="Please select key" options={keys} value=""/>
                    <button className="btn btn-success" onClick={this.handleSubmit}>Save</button>
                </form>
            )
        } else
            return null;
    }
}