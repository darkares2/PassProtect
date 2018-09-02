const React = require('react');
const axios = require('axios');
import {App} from "../app";
import {Password} from '../entities/password';

export class PasswordList extends React.Component{
    constructor(props) {
        super(props);
        this.state = {passwords: [], filter: ""};
        this.handleChange = this.handleChange.bind(this);
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

    handleChange (event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;
        const name = target.name;
        this.setState({[name]: value});
    }

    render() {
        const passwords = this.state.passwords.map(password => {
            return (password.name.toLowerCase().includes(this.state.filter.toLowerCase()) || password.description.toLowerCase().includes(this.state.filter.toLowerCase())) ?
                <Password key={password.id} item={password} safeMode={this.props.safeMode}/>
                : null
        });
        return (
            <div>
                <div className="form-group input-group-sm col-2 offset-4">
                <input type="text" className="form-control" id="filter" name="filter" placeholder="Filter..." value={this.state.filter} onChange = {this.handleChange}/>
                </div>
            <table className="table table-bordered table-striped">
                {Password.header(this.props.safeMode)}
                <tbody>
                {passwords}
                </tbody>
            </table>
            </div>
        )
    }
}
