const React = require('react');
import {Employee} from '../entities/employee.js';

export class EmployeeList extends React.Component{
    render() {
        var employees = this.props.employees.map(employee =>
            <Employee key={employee._links.self.href} employee={employee}/>
        );
        return (
            <table className="table table-bordered table-striped">
                <tbody>
                <tr>
                    <th>Given Name</th>
                    <th>SurName</th>
                    <th>Description</th>
                </tr>
                {employees}
                </tbody>
            </table>
        )
    }
}
