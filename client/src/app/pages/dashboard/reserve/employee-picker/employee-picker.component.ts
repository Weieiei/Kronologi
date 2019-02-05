import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { EmployeeDTO } from '../../../../interfaces/user/employee-dto';
import { Observable, Subscription } from 'rxjs';

@Component({
    selector: 'app-employee-picker',
    templateUrl: './employee-picker.component.html',
    styleUrls: ['./employee-picker.component.scss']
})
export class EmployeePickerComponent implements OnInit, OnDestroy {

    @Input() employees: EmployeeDTO[];

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

    selectEmployee(employee: EmployeeDTO) {
        this.employeeChange.emit(employee);
    }

    // todo: Allow user to view more info on employee availabilities?
}
