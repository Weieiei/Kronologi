import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

interface Time {
    hour: number;
    minute: string;
}

@Component({
    selector: 'app-time-picker',
    templateUrl: './time-picker.component.html',
    styleUrls: ['./time-picker.component.scss']
})
export class TimePickerComponent implements OnInit {

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
        this.generateTimes();
    }

    ngOnInit() {
    }

    generateTimes(): void {

        this.morningTimes = [];
        this.afterNoonTimes = [];
        this.eveningTimes = [];

        for (let i = this.minHour; i <= 11; i++) {
            this.quarters.forEach(quarter => this.morningTimes.push({ hour: i, minute: quarter }));
        }
        for (let i = 12; i <= 16; i++) {
            this.quarters.forEach(quarter => this.afterNoonTimes.push({ hour: i, minute: quarter }));
        }
        for (let i = 17; i <= this.maxHour; i++) {
            this.quarters.forEach(quarter => this.eveningTimes.push({ hour: i, minute: quarter }));
        }

    }

    selectTime(time: Time) {
        this.timeChange.emit(`${time.hour}:${time.minute}`);
    }

}
