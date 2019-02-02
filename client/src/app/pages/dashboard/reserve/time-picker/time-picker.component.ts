import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import * as moment from 'moment';

interface Time {
    hour: number;
    minute: string;
    enabled: boolean;
}

@Component({
    selector: 'app-time-picker',
    templateUrl: './time-picker.component.html',
    styleUrls: ['./time-picker.component.scss']
})
export class TimePickerComponent implements OnInit, OnChanges {

    @Input() employeeShift: any;
    @Input() employeeAppointments: any;

    @Output() timeChange = new EventEmitter();

    minHour = 9;
    maxHour = 20;
    // quarters: string[] = ['00', '15', '30', '45'];
    quarters: string[] = ['00', '30'];

    morningTimes: Time[];
    afterNoonTimes: Time[];
    eveningTimes: Time[];

    constructor() {
    }

    ngOnInit() {
    }

    ngOnChanges() {
        if (this.employeeShift) {
            this.generateTimes();
        }
    }

    generateTimes(): void {

        this.morningTimes = [];
        this.afterNoonTimes = [];
        this.eveningTimes = [];

        for (let i = this.minHour; i <= 11; i++) {
            this.quarters.forEach(quarter => {
                const enabled = this.isBetweenShiftTimes(`${i}:${quarter}`);
                this.morningTimes.push({ hour: i, minute: quarter, enabled });
            });
        }

        for (let i = 12; i <= 16; i++) {
            this.quarters.forEach(quarter => {
                const enabled = this.isBetweenShiftTimes(`${i}:${quarter}`);
                this.afterNoonTimes.push({ hour: i, minute: quarter, enabled });
            });
        }

        for (let i = 17; i <= this.maxHour; i++) {
            this.quarters.forEach(quarter => {
                const enabled = this.isBetweenShiftTimes(`${i}:${quarter}`);
                this.eveningTimes.push({ hour: i, minute: quarter, enabled });
            });
        }

    }

    selectTime(time: Time) {
        if (time.enabled) {
            this.timeChange.emit(`${time.hour}:${time.minute}`);
        }
    }

    isBetweenShiftTimes(t: string): boolean {
        const format = 'hh:mm';

        const time = moment(t, format);
        const startTime = moment(this.employeeShift.startTime, format);
        const endTime = moment(this.employeeShift.endTime, format);

        return time.isBetween(startTime, endTime) || time.isSame(startTime);
    }

}
