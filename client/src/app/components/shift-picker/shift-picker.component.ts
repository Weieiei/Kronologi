import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import * as moment from 'moment';
import { MatDialog } from '@angular/material';
import { TimePickerDialogComponent } from './time-picker-dialog/time-picker-dialog.component';

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
    maxDate: Date = new Date(moment(this.minDate).add(5, 'months').format('YYYY-MM-DD'));

    date: string;
    startTime: string;
    endTime: string;

    @ViewChild('dateInput') dateInput: ElementRef;

    constructor(public dialog: MatDialog) {
    }

    ngOnInit() {
    }

    setDate(date: string) {
        console.log(date);
    }

    openTimePicker(): void {

        const dialogRef = this.dialog.open(TimePickerDialogComponent, {
            width: '650px',
            data: { startTime: this.startTime, endTime: this.endTime }
        });

        dialogRef.afterClosed().subscribe(result => {
            this.date = moment(new Date(this.dateInput.nativeElement.value)).format('YYYY-MM-DD');
            this.startTime = result.startTime;
            this.endTime = result.endTime;
        });

    }

}
