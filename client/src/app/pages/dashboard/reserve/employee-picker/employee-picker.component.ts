import { Component, OnInit } from '@angular/core';
import { EmployeeService } from '../../../../services/employee/employee.service';

@Component({
  selector: 'app-employee-picker',
  templateUrl: './employee-picker.component.html',
  styleUrls: ['./employee-picker.component.scss']
})
export class EmployeePickerComponent implements OnInit {

  employees: string[];

  constructor(private employeeService: EmployeeService) { }

  ngOnInit() {
    this.employeeService.getAvailableEmployees(null).subscribe( res => this.employees = res); // todo
  }

  // Allow user to view more info on employee availabilities?
}
