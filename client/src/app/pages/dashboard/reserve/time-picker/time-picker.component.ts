import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';
import * as moment from 'moment';
import { Moment } from 'moment';

interface Time {
    hour: number;
    minute: number;
    enabled: boolean;
}

@Component({
    selector: 'app-time-picker',
    templateUrl: './time-picker.component.html',
    styleUrls: ['./time-picker.component.scss']
})
export class TimePickerComponent implements OnInit, OnChanges {

    @Input() service: any;
    @Input() employeeShift: any;
    @Input() employeeAppointments: any[];

    @Output() timeChange = new EventEmitter();

    minHour = 8;
    maxHour = 20;
    incrementBy = 30;
    minuteIncrements: number[] = Array(60 / this.incrementBy).fill(0).map((x, y) => x + this.incrementBy * y);

    morningTimes: Time[];
    afterNoonTimes: Time[];
    eveningTimes: Time[];

    constructor() {
    }

    ngOnInit() {
    }

    ngOnChanges() {
        if (this.employeeShift && this.employeeAppointments) {
            this.generateTimes();
        }
    }

    generateTimes(): void {

        this.morningTimes = [];
        this.afterNoonTimes = [];
        this.eveningTimes = [];

        for (let hour = this.minHour; hour <= 11; hour++) {
            this.minuteIncrements.forEach(minute => {
                const enabled = this.isValid(`${hour}:${minute}`);
                this.morningTimes.push({ hour, minute, enabled });
            });
        }

        for (let hour = 12; hour <= 16; hour++) {
            this.minuteIncrements.forEach(minute => {
                const enabled = this.isValid(`${hour}:${minute}`);
                this.afterNoonTimes.push({ hour, minute, enabled });
            });
        }

        for (let hour = 17; hour <= this.maxHour; hour++) {
            this.minuteIncrements.forEach(minute => {
                const enabled = this.isValid(`${hour}:${minute}`);
                this.eveningTimes.push({ hour, minute, enabled });
            });
        }

    }

    selectTime(time: Time) {
        if (time.enabled) {
            this.timeChange.emit(`${time.hour}:${time.minute}`);
        }
    }

    isValid(t: string): boolean {
        const format = 'hh:mm';
        const time = moment(t, format);

        const isBetweenShift = this.isBetweenShift(format, time);

        let isDuringAnAppointment: boolean;
        if (isBetweenShift) {
            isDuringAnAppointment = this.isDuringAnAppointment(format, time);
        }

        // let canFitServiceIntoEmployeesSchedule: boolean;
        // if (isBetweenShift && !isDuringAnAppointment) {
        //     canFitServiceIntoEmployeesSchedule = this.canFitServiceIntoEmployeesSchedule(format, time);
        // }

        return isBetweenShift && !isDuringAnAppointment;
    }

    isBetweenShift(format: string, time: Moment): boolean {
        const startTime = moment(this.employeeShift.startTime, format);
        const endTime = moment(this.employeeShift.endTime, format);
        return time.isBetween(startTime, endTime) || time.isSame(startTime);
    }

    isDuringAnAppointment(format: string, time: Moment): boolean {
        return this.employeeAppointments.some(appointment => {
            const startTime = moment(appointment.startTime, format);
            const endTime = moment(appointment.endTime, format);
            return time.isBetween(startTime, endTime) || time.isSame(startTime);
        });
    }

    canFitServiceIntoEmployeesSchedule(format: string, time: Moment): boolean {
        return;
    }

}
