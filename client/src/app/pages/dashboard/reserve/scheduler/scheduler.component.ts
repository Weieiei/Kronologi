import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription } from 'rxjs';

@Component({
    selector: 'app-scheduler',
    templateUrl: './scheduler.component.html',
    styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit, OnDestroy {

    year: number;
    month: number;

    dateSubscription: Subscription;
    @Input() dateEvent: Observable<string>;

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
        this.dateSubscription = this.dateEvent.subscribe(res => {
            const date = new Date(res + ' EST');
            this.year = date.getFullYear();
            this.month = date.getMonth();

            this.appointmentYear = this.year;
            this.appointmentMonth = this.month;
            this.appointmentDay = date.getDate();
        });
    }

    ngOnDestroy() {
        this.dateSubscription.unsubscribe();
    }

    setDay(day: any): void {
        const date: Date = new Date(this.year, this.month, day);
        this.dateChange.emit(date);
    }
}
