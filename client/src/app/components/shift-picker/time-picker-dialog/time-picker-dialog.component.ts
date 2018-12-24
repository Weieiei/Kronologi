import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { DialogData } from '../shift-picker.component';

@Component({
    selector: 'app-time-picker-dialog',
    templateUrl: './time-picker-dialog.component.html',
    styleUrls: ['./time-picker-dialog.component.scss']
})
export class TimePickerDialogComponent implements OnInit {

    minHour = 9;
    maxHour = 22;

    quarters: string[] = ['00', '15', '30', '45'];
    hours: number[] = Array(this.maxHour - this.minHour).fill(this.minHour).map((x, y) => x + y);
    times: string[] = [];

    selectedStartTime: number;
    selectedEndTime: number;

    constructor(
        public dialogRef: MatDialogRef<TimePickerDialogComponent>,
        @Inject(MAT_DIALOG_DATA) public dialogData: DialogData
    ) {
        this.hours.forEach(hour => this.quarters.forEach(quarter => this.times.push(`${hour}:${quarter}`)));
        this.times.push(`${this.maxHour}:${this.quarters[0]}`);
    }

    ngOnInit() {
    }

    pickTime(i: number, time: string): void {

        if (this.selectedStartTime === i) {

            this.selectedStartTime = void 0;
            this.dialogData.startTime = void 0;

        } else if (this.selectedEndTime === i) {

            this.selectedEndTime = void 0;
            this.dialogData.endTime = void 0;

        } else if (this.selectedStartTime === void 0) {

            if (this.selectedEndTime <= i) {
                return;
            }

            this.selectedStartTime = i;
            this.dialogData.startTime = time;

        } else {

            this.selectedEndTime = i;
            this.dialogData.endTime = time;

        }

    }

    cancel(): void {
        this.dialogRef.close();
    }

}
