import {App} from "../app";

const React = require('react');
const axios = require('axios');

export class Password extends React.Component{
    constructor(props) {
        super(props);
        this.state = { deleted: false};
        this.handleDelete = this.handleDelete.bind(this);
    }

    handleDelete() {
        const self = this;
        axios.delete('/api/v1/password/' + this.props.item.id, {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({deleted: true});
            })
            .catch(function (error) {
                App.handleErrorResponse(error, "Failed to delete password")
            });
    }

    static header() {
        return (
            <thead>
            <tr><th>Name</th><th></th></tr>
            </thead>
        )
    }
    render() {
        if (!this.state.deleted) {
            return (
                <tr>
                    <td>{this.props.item.name}</td>
                    <td>
                        <button className="btn btn-danger btn-xs" onClick={this.handleDelete}>Del</button>
                    </td>
                </tr>
            )
        } else
            return null;
    }
}