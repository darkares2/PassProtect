const React = require('react');
const axios = require('axios');

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
        axios.post('/login', payload, {
            headers: {
                'accept': 'application/json',
            //    'content-type': 'application/x-www-form-urlencoded'
            }
            })
            .then(function (response) {
                console.log(response);
                if(response.data.code === 200){
                    console.log("Login successfull " + response.data);
                    window.location.replace("/");
                }
                else if(response.data.code === 403){
                    console.log("Username password do not match");
                    alert("username password do not match")
                }
                else{
                    console.log("Username does not exists");
                    alert("Username does not exist");
                }
            })
            .catch(function (error) {
                console.log(error);
            });
    }


    render() {
        return (
            <div>
                <form className="form">
                    <div>
                        <input id="username" name="username" onChange = {this.handleChange} />
                        <br/>
                        <input id="password" name="password" type="password" onChange = {this.handleChange} />
                        <br/>
                        <button className="btn btn-xs btn-success" type="submit" onClick={this.handleSubmit}/>
                    </div>
                </form>
            </div>
        );
    }
}
