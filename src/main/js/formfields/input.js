const React = require('react');

export class Input extends React.Component{
    constructor(props) {
        super(props);
        this.state = {value: props.value};
        this.handleChange = this.handleChange.bind(this);
    }

    value() {
        return this.state.value;
    }

    handleChange (event) {
        const target = event.target;
        const value = target.type === 'checkbox' ? target.checked : target.value;

        this.setState({value: value});
    }

    render() {
            return (
                <div className="form-group">
                    <label htmlFor={this.props.id}>{this.props.label}</label>
                    <input type={this.props.type} className="form-control" autocomplete={this.props.autocomplete} id={this.props.id} name={this.props.id} aria-describedby={this.props.id + "Help"} placeholder={this.props.placeholder} value={this.state.value} onChange = {this.handleChange}/>
                        <small id={this.props.id + "Help"} className="form-text text-muted">{this.props.help}</small>
                </div>
                        )
    }
}