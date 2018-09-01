const React = require('react');
const axios = require('axios');
import {App} from "../app";
import {Key} from '../entities/key';

export class KeyList extends React.Component{
    constructor(props) {
        super(props);
        this.state = {keys: []};
    }

    componentDidMount() {
        this.loadKeys();
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

    componentWillReceiveProps(props) {
        const { refresh, id } = this.props;
        if (props.refresh !== refresh) {
            this.loadKeys();
        }
    }

    render() {
        var keys = this.state.keys.map(key =>
            <Key key={key.id} item={key}/>
        );
        return (
            <table className="table table-bordered table-striped">
                {Key.header()}
                <tbody>
                {keys}
                </tbody>
            </table>
        )
    }
}
