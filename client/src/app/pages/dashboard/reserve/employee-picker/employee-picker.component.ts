import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { EmployeeDTO } from '../../../../interfaces/user/employee-dto';

@Component({
    selector: 'app-employee-picker',
    templateUrl: './employee-picker.component.html',
    styleUrls: ['./employee-picker.component.scss']
})
export class EmployeePickerComponent implements OnInit {

    @Input() employees: EmployeeDTO[];
    @Input() employeeId?: number;

    @Output() employeeChange = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
    }

    selectEmployee(employee: EmployeeDTO) {
        this.employeeChange.emit(employee);
    }

    // todo: Allow user to view more info on employee availabilities?
}
