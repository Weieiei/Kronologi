import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-scheduler',
    templateUrl: './scheduler.component.html',
    styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit {

    year: number;
    month: number;

    // We could possibly pass in a date to the component, if we're in the process of modifying an appointment
    // appointmentX variables represent the original chosen date of the appointment we're modifying
    @Input() appointmentDate?: string;

    @Output() dateChange = new EventEmitter();

    constructor() {
        const currentDate = new Date();
        this.year = currentDate.getFullYear();
        this.month = currentDate.getMonth();
    }

    ngOnInit() {
        setTimeout(() => {
            if (this.appointmentDate) {
                const date = new Date(this.appointmentDate);
                this.year = date.getFullYear();
                this.month = date.getMonth();
            }
        });
    }

    setDay(day: any): void {
        const date: Date = new Date(this.year, this.month, day);
        this.dateChange.emit(date);
    }
}
