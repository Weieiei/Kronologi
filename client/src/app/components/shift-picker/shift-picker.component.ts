import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import * as moment from 'moment';
import { MatDialog } from '@angular/material';
import { TimePickerDialogComponent } from './time-picker-dialog/time-picker-dialog.component';
import { EmployeeShiftTimes } from '../../models/shift/EmployeeShiftTimes';

export interface DialogData {
    startTime: string;
    endTime: string;
}

@Component({
    selector: 'app-shift-picker',
    templateUrl: './shift-picker.component.html',
    styleUrls: ['./shift-picker.component.scss']
})
export class ShiftPickerComponent implements OnInit {

    minDate: Date = new Date();
    maxDate: Date = new Date(moment(this.minDate).add(5, 'months').format());

    /**
     * Since the register component (for adding an employee) contains multiple shift-picker components, we need a way to track which one
     * we're manipulating. We can do this by attaching a unique number to each individual shift object emitted by the component.
     */
    @Input() number: number;
    defaultDate: Date;

    date: string;
    startTime: string;
    endTime: string;

    @Output() shiftOutput = new EventEmitter();

    constructor(public dialog: MatDialog) {
    }

    ngOnInit() {
        this.defaultDate = new Date(moment(this.minDate).add(this.number, 'days').format());
    }

    openTimePicker(): void {

        const dialogRef = this.dialog.open(TimePickerDialogComponent, {
            width: '650px',
            data: { startTime: this.startTime, endTime: this.endTime }
        });

        dialogRef.afterClosed().subscribe(result => {

            try {

                this.date = moment(new Date(this.defaultDate)).format('YYYY-MM-DD');
                this.startTime = `${this.date} ${result.startTime}`;
                this.endTime = `${this.date} ${result.endTime}`;

                this.shiftOutput.emit(new EmployeeShiftTimes(this.number, new Date(this.startTime), new Date(this.endTime)));

            } catch (e) {
                alert('Please make sure to properly pick start and end times.');
            }

        });

    }

}
