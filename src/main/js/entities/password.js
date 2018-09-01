import {App} from "../app";

const React = require('react');
const axios = require('axios');

export class Password extends React.Component{
    constructor(props) {
        super(props);
        this.state = { deleted: false, deletePrep: false};
        this.handleDeletePrep = this.handleDeletePrep.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleCopy = this.handleCopy.bind(this);
        this.handleUndo = this.handleUndo.bind(this);
        this.copyText = this.copyText.bind(this);
    }

    handleUndo() {
        console.log("Fuck off!");
    }
    handleDeletePrep() {
        this.setState({deletePrep: true});
    }
    handleDelete() {
        this.setState({deletePrep: false});
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
                App.showMessageWithButton('success', 'Password deleted', 'Undo', self.handleUndo);
            })
            .catch(function (error) {
                App.handleErrorResponse('error', "Failed to delete password")
            });
    }

    copyText() {
        let textField = document.createElement('textarea');
        textField.innerText = this.state.decrypted;
        document.body.appendChild(textField);
        textField.select();
        document.execCommand('copy');
        textField.remove();
        this.setState({decrypted: undefined});
    }

    handleCopy() {
        const self = this;
        axios.get('/api/v1/password/decrypt/' + this.props.item.id, {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({decrypted: response.data});
            })
            .catch(function (error) {
                App.handleErrorResponse(error, "Failed to fetch decrypted password")
            });
    }

    static header() {
        return (
            <thead>
            <tr><th>Name</th><th>Description</th><th></th><th></th></tr>
            </thead>
        )
    }
    render() {
        if (!this.state.deleted) {
            let copyButton = null;
            let deleteButton = null;
            if (this.state.deletePrep)
                deleteButton = (<button className="btn btn-danger btn-sm" onClick={this.handleDelete}>Are you sure?</button>);
            else
                deleteButton = (<button className="btn btn-danger btn-sm" onClick={this.handleDeletePrep}>Delete</button>);
            if (this.state.decrypted !==undefined)
                copyButton = (<button className="btn btn-info btn-sm" onClick={this.copyText}>Copy</button>);
            else
                copyButton = (<button className="btn btn-info btn-sm" onClick={this.handleCopy}>Prep</button>);
            return (
                <tr>
                    <td>{this.props.item.name}</td>
                    <td>{this.props.item.description}</td>
                    <td>
                        {deleteButton}
                    </td>
                    <td>
                        {copyButton}
                    </td>
                </tr>
            )
        } else
            return null;
    }
}