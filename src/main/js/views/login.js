const React = require('react');
const axios = require('axios');
import {App} from '../app'

export class Login extends React.Component {

    constructor(props){
        super(props);
        this.state={
            username:'',
            password:''
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange (event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }

    handleSubmit(event){
        event.preventDefault();
        const self = this;
        const payload={
            "username":this.state.username,
            "password":this.state.password
        }
        /*let bodyFormData = new FormData();
        bodyFormData.set('username', this.state.username);
        bodyFormData.set('password', this.state.password);*/
        axios.post('/loginTest', payload, {
            headers: {
                'accept': 'application/json',
                //'content-type': 'application/x-www-form-urlencoded'
            }
            })
            .then(function (response) {
                console.log(response);
                if (response.data.code === 200) {
                    console.log("Login successfull " + response.data);
                    window.location.replace("/");
                } else if (response.data.error !== undefined)
                    App.showMessage("error", "Login failed", response.data.error);
                else
                    console.log("Something went wrong");
            })
            .catch(function (error) {
                console.log(error);
                App.showMessage("error", "Login failed", error);
            });
    }


    render() {
        return (
            <div>
                <form className="form-inline">
                    <div className="form-group">
                        <input className="form-control" id="username" name="username" onChange = {this.handleChange} />
                        <input className="form-control" id="password" name="password" type="password" onChange = {this.handleChange} />
                        <button className="btn btn-xs btn-success" type="submit" onClick={this.handleSubmit}>Login</button>
                    </div>
                </form>
            </div>
        );
    }
}
