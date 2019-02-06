import { Component, EventEmitter, Input, OnChanges, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.scss']
})
export class CalendarComponent implements OnInit, OnChanges {

    @Input() year: number;
    @Input() month: number;

    @Input() appointmentYear?: number;
    @Input() appointmentMonth?: number;
    @Input() appointmentDay?: number;

    @Output() dayChange = new EventEmitter();

    daysOfWeek = [
        // 'Sunday',
        // 'Monday',
        // 'Tuesday',
        // 'Wednesday',
        // 'Thursday',
        // 'Friday',
        // 'Saturday'
        'Sun',
        'Mon',
        'Tue',
        'Wed',
        'Thu',
        'Fri',
        'Sat'
    ];

    spacer: number;
    days: any[] = [];

    private currentDate: Date = new Date();

    constructor() { }

    ngOnInit() {
        setTimeout(() => {
            if (this.appointmentYear && this.appointmentMonth && this.appointmentDay) {
                this.year = this.appointmentYear;
                this.month = this.appointmentMonth;
                this.updateCalendar();
            }
        });
    }

    ngOnChanges() {
        this.updateCalendar();
    }

    updateCalendar() {
        this.days = [];
        this.setSpacerLength();
        this.setNumberOfDays();
    }

    private setNumberOfDays(): void {
        const numOfDays = new Date(this.year, this.month + 1, 0).getDate();

        for (let i = 1; i <= numOfDays; i++) {
            this.days.push({date: i, enabled: this.isEnabled(i)});
        }
    }

    private isEnabled(date: number): boolean {
        if (this.year > this.currentDate.getFullYear()) {
            return true;
        } else {
            const yearGood = this.year >= this.currentDate.getFullYear();

            if (yearGood && this.month > this.currentDate.getMonth()) {
                return true;
            } else if (yearGood && this.month >= this.currentDate.getMonth() && date >= this.currentDate.getDate()) {
                return true;
            }
        }

        return false;
    }

    private setSpacerLength(): void {
        this.spacer = new Date(this.year, this.month, 1).getDay();
    }

    handleDateClick(date: any) {
        if (date.enabled) {
            this.dayChange.emit(date.date);
        }
    }
}
