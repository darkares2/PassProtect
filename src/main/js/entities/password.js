import {App} from "../app";

const React = require('react');
const axios = require('axios');

export class Password extends React.Component{
    constructor(props) {
        super(props);
        this.state = { deleted: false, deletePrep: false, genPrep: false};
        this.handleDeletePrep = this.handleDeletePrep.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleGenPrep = this.handleGenPrep.bind(this);
        this.handleGen = this.handleGen.bind(this);
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

    handleGenPrep() {
        this.setState({genPrep: true});
    }
    handleGen() {
        this.setState({genPrep: false});
        const self = this;
        axios.put('/api/v1/password/regen/' + this.props.item.id, {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                App.showMessage('success', 'Success!', 'Password regenerated');
            })
            .catch(function (error) {
                App.handleErrorResponse('error', "Failed to regenerate password")
            });
    }

    copyText() {
        let textField = document.createElement('textarea');
        textField.innerText = this.state.decrypted;
        document.body.appendChild(textField);
        textField.select();
        let iOS = !!navigator.platform && /iPad|iPhone|iPod/.test(navigator.platform);
        if (iOS)
            this.iosCopyToClipboard(textField);
        document.execCommand('copy');
        textField.remove();
        this.setState({decrypted: undefined});
    }

    iosCopyToClipboard(el) {
        var oldContentEditable = el.contentEditable,
            oldReadOnly = el.readOnly,
            range = document.createRange();

        el.contentEditable = true;
        el.readOnly = false;
        range.selectNodeContents(el);

        var s = window.getSelection();
        s.removeAllRanges();
        s.addRange(range);

        el.setSelectionRange(0, 999999); // A big number, to cover anything that could be inside the element.

        el.contentEditable = oldContentEditable;
        el.readOnly = oldReadOnly;
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

    static header(safeMode) {
        if (safeMode)
            return (
                <thead>
                <tr><th>Name</th><th>Description</th><th></th></tr>
                </thead>
            )
        else
            return (
                <thead>
                <tr><th>Name</th><th>Description</th><th></th><th></th><th></th></tr>
                </thead>
            )
    }
    render() {
        if (!this.state.deleted) {
            let copyButton = null;
            let deleteButton = null;
            let genButton = null;
            if (!this.props.safeMode) {
                if (this.state.genPrep)
                    genButton = (<td>
                        <button className="btn btn-warning btn-sm" onClick={this.handleGen}>Are you sure?</button>
                    </td>);
                else
                    genButton = (<td>
                        <button className="btn btn-warning btn-sm" onClick={this.handleGenPrep}>Regenerate</button>
                    </td>);
                if (this.state.deletePrep)
                    deleteButton = (<td>
                        <button className="btn btn-danger btn-sm" onClick={this.handleDelete}>Are you sure?</button>
                    </td>);
                else
                    deleteButton = (<td>
                        <button className="btn btn-danger btn-sm" onClick={this.handleDeletePrep}>Delete</button>
                    </td>);
            }
            if (this.state.decrypted !== undefined)
                copyButton = (<td>
                    <button className="btn btn-info btn-sm" onClick={this.copyText}>Copy</button>
                </td>);
            else
                copyButton = (<td>
                    <button className="btn btn-info btn-sm" onClick={this.handleCopy}>Prep</button>
                </td>);
            return (
                <tr>
                    <td>{this.props.item.name}</td>
                    <td>{this.props.item.description}</td>
                    {deleteButton}
                    {copyButton}
                    {genButton}
                </tr>
            )
        } else
            return null;
    }
}