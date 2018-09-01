const React = require('react');
import {Header} from './html/header';
import {Main} from './html/main';
const NotificationSystem = require('react-notification-system');

let notif = null;

export class App extends React.Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        notif = this.refs.notificationSystem;
    }

    static showMessageWithButton(level, message, label, callback) {
        notif.addNotification({
            message: message,
            level: level,
            action: {
                label: label,
                callback: callback
            }
        });
    }

    static showMessage(level, message, extra) {
        notif.addNotification({
            message: message,
            level: level,
            children: (
                <div>
                    <p>{extra}</p>
                </div>
            )
        });
    }

    static handleErrorResponse(response, error) {
        let message = "";
        if (response.response !== undefined && response.response.data !== undefined && response.response.data.message !== undefined)
            message = response.response.data.message;
        else if (response.error !== undefined)
            message = response.error;
        else if (response.message !== undefined)
            message = response.message;
        else
            message = response;

        notif.addNotification({
            message: error,
            level: 'error',
            children: (
                <div>
                    <p>{message}</p>
                </div>
            )
        });
    }

    render() {
        return (
            <div>
                <Header/>
                <Main/>
                <NotificationSystem ref="notificationSystem" />
            </div>
        )
    }
}



