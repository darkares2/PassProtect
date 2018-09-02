import {App} from "../app";

const React = require('react');
const axios = require('axios');

export class Event extends React.Component{
    constructor(props) {
        super(props);
    }

    static header() {
        return (
            <thead>
            <tr><th>Time</th><th>Event</th><th>Message</th></tr>
            </thead>
        )
    }
    render() {
            return (
                <tr>
                    <td>{new Date(this.props.item.timestamp).toLocaleString('da-DK')}</td>
                    <td>{this.props.item.event}</td>
                    <td>{this.props.item.message}</td>
                </tr>
            )
    }
}