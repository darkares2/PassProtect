const React = require('react');
const axios = require('axios');
import {App} from "../app";
import {Event} from '../entities/event';

export class EventList extends React.Component{
    constructor(props) {
        super(props);
        this.state = {events: []};
    }

    componentDidMount() {
        this.loadEvents();
    }

    loadEvents() {
        const self = this;
        axios.get('/api/v1/event/all', {
            headers: {
                'accept': 'application/json',
                //    'content-type': 'application/x-www-form-urlencoded'
            }
        })
            .then(function (response) {
                console.log(response);
                self.setState({events: response.data});
            })
            .catch(function (error) {
                console.log(error);
                App.handleErrorResponse(error, "Failed to load events")
            });
    }

    componentWillReceiveProps(props) {
        const { refresh, id } = this.props;
        if (props.refresh !== refresh) {
            this.loadEvents();
        }
    }

    render() {
        var events = this.state.events.map(event =>
            <Event key={event.id} item={event}/>
        );
        return (
            <table className="table table-bordered table-striped">
                {Event.header()}
                <tbody>
                {events}
                </tbody>
            </table>
        )
    }
}
