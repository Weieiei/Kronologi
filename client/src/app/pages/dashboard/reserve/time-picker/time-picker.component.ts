import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Time } from '../../../../interfaces/time';

@Component({
    selector: 'app-time-picker',
    templateUrl: './time-picker.component.html',
    styleUrls: ['./time-picker.component.scss']
})
export class TimePickerComponent implements OnInit, OnDestroy {

    @Output() timeChange = new EventEmitter();

    startTime: string;
    startTimeSubscription: Subscription;
    @Input() startTimeEvent: Observable<string>;

    @Input() times: Time[] = [];

    constructor() {
    }

    ngOnInit() {
        this.startTimeSubscription = this.startTimeEvent.subscribe(res => this.startTime = res);
    }

    ngOnDestroy() {
        this.startTimeSubscription.unsubscribe();
    }

    morningTimes(): Time[] {
        return this.times.filter(time => time.hour < 12);
    }

    afterNoonTimes(): Time[] {
        return this.times.filter(time => time.hour >= 12 && time.hour <= 16);
    }

    eveningTimes(): Time[] {
        return this.times.filter(time => time.hour > 16);
    }

    selectTime(time: Time) {
        if (time.enabled) {
            this.timeChange.emit(`${time.hour < 10 ? '0' + time.hour : time.hour}:${time.minute < 10 ? '0' + time.minute : time.minute}`);
        }
    }

}
