import {Password} from "../entities/password";

const React = require('react');

export class Select extends React.Component{
    constructor(props) {
        super(props);
        let value = this.props.value;
        if (value === undefined || value === "") {
            value = this.props.options[0].value;
        }
        this.state = {value: value};
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
        const keyOptions = this.props.options.map(option =>
            <option key={option.value} value={option.value}>{option.text}</option>
        );
        let outerClass = "form-group";
        let inputDivClass = "";
        if (this.props.formType === 'row') {
            outerClass += " row";
            inputDivClass = "col-sm-10";
        }

        return (
                <div className={outerClass}>
                    <label htmlFor={this.props.id}>{this.props.label}</label>
                    <div className={inputDivClass}>
                    <select className="form-control" autoComplete={this.props.autocomplete} id={this.props.id} name={this.props.id} value={this.state.value} aria-describedby={this.props.id + "Help"} onChange = {this.handleChange}>
                        {keyOptions}
                    </select>
                    </div>
                        <small id={this.props.id + "Help"} className="form-text text-muted">{this.props.help}</small>
                </div>
                        )
    }
}