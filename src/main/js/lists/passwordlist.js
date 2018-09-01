const React = require('react');
const axios = require('axios');
import {App} from "../app";
import {Password} from '../entities/password';

export class PasswordList extends React.Component{
    constructor(props) {
        super(props);
        this.state = {passwords: []};
    }

    componentDidMount() {
        this.loadPasswords();
    }

    loadPasswords() {
        const self = this;
        axios.get('/api/v1/password/all', {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({passwords: response.data});
            })
            .catch(function (error) {
                console.log(error);
                App.handleErrorResponse(error, "Failed to load passwords")
            });
    }

    componentWillReceiveProps(props) {
        const { refresh, id } = this.props;
        if (props.refresh !== refresh) {
            this.loadPasswords();
        }
    }

    render() {
        const passwords = this.state.passwords.map(password =>
            <Password key={password.id} item={password}/>
        );
        return (
            <table className="table table-bordered table-striped">
                {Password.header()}
                <tbody>
                {passwords}
                </tbody>
            </table>
        )
    }
}
