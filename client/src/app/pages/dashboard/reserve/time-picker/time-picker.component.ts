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

    timeFormat = 'hh:mm';
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
        const time = moment(t, this.timeFormat);

        if (!this.isWithinShift(time)) {
            return false;
        }

        if (this.isDuringAnAppointment(time)) {
            return false;
        }

        return this.canFitServiceIntoEmployeesSchedule(time);

    }

    isWithinShift(time: Moment): boolean {
        const startTime = moment(this.employeeShift.startTime, this.timeFormat);
        const endTime = moment(this.employeeShift.endTime, this.timeFormat);
        return time.isBetween(startTime, endTime) || time.isSame(startTime) || time.isSame(endTime);
    }

    isDuringAnAppointment(time: Moment): boolean {
        return this.employeeAppointments.some(appointment => {
            const startTime = moment(appointment.startTime, this.timeFormat);
            const endTime = moment(appointment.endTime, this.timeFormat);
            return time.isBetween(startTime, endTime) || time.isSame(startTime);
        });
    }

    canFitServiceIntoEmployeesSchedule(time: Moment): boolean {
        let duration = this.service.duration;
        while (duration !== 0) {

            // 5 because the greatest common divisor of service durations is 5
            time.add(5, 'm');

            if (!this.isWithinShift(time)) {
                return false;
            }

            if (this.isDuringAnAppointment(time)) {
                return false;
            }

            duration -= 5;
        }

        return true;
    }

}
