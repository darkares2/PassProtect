const React = require('react');
const axios = require('axios');
import {Input} from '../formfields/input'
import {App} from '../app'

export class KeyForm extends React.Component{
    constructor(props) {
        super(props);
        this.state = {keys: [], displayAddForm: false};
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();
        const self = this;

        let name = this.refs.name.value();
        console.log("Name is " + name)

        axios.post('/api/v1/key/', { name: name}, {
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
                <form>
                    <Input ref="name" id="name" type="text" label="Name" placeholder="Keyname" help="Please enter the name of the key" value=""/>
                    <button className="btn btn-success" onClick={this.handleSubmit}>Save</button>
                </form>
            )
        } else
            return null;
    }
}