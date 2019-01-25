import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-scheduler',
    templateUrl: './scheduler.component.html',
    styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit {

    year: number;
    month: number;

    // We could possible pass in a date to the component, if we're in the process of modifying an appointment
    // appointmentX variables represent the chosen date of the appointment we're modifying
    @Input() date?: string;
    appointmentYear: number;
    appointmentMonth: number;
    appointmentDay: number;

    @Output() dateChange = new EventEmitter();

    constructor() {
        const currentDate = new Date();
        this.year = currentDate.getFullYear();
        this.month = currentDate.getMonth();
    }

    ngOnInit() {
        if (this.date) {
            const date = new Date(this.date + ' EST');
            this.appointmentYear = date.getFullYear();
            this.appointmentMonth = date.getMonth();
            this.appointmentDay = date.getDate();
        }
    }

    setDay(day: any): void {
        const date: Date = new Date(this.year, this.month, day);
        this.dateChange.emit(date);
    }
}
