import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { EmployeeTimes } from '../../../../interfaces/employee/employee-times';

@Component({
    selector: 'app-employee-picker',
    templateUrl: './employee-picker.component.html',
    styleUrls: ['./employee-picker.component.scss']
})
export class EmployeePickerComponent implements OnInit, OnDestroy {

    @Input() employees: EmployeeTimes[];

    employeeId: number;
    employeeSubscription: Subscription;
    @Input() employeeEvent: Observable<number>;

    @Output() employeeChange = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
        this.employeeSubscription = this.employeeEvent.subscribe(res => this.employeeId = res);
    }

    ngOnDestroy() {
        this.employeeSubscription.unsubscribe();
    }

    selectEmployee(employee: EmployeeTimes) {
        if (!this.notAvailable(employee)) {
            this.employeeChange.emit(employee);
        }
    }

    notAvailable(employee: EmployeeTimes): boolean {
        return employee.times.every(time => !time.enabled);
    }

    // todo: Allow user to view more info on employee availabilities?
}
