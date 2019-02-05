import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output } from '@angular/core';
import * as moment from 'moment';
import { Moment } from 'moment';
import { ShiftDTO } from '../../../../interfaces/shift-dto/shift-dto';
import { ServiceDTO } from '../../../../interfaces/service/service-dto';
import { EmployeeAppointmentDTO } from '../../../../interfaces/appointment/employee-appointment-dto';
import { Observable, Subscription } from 'rxjs';
import { Time } from '../../../../interfaces/time';
import { HelperService } from '../../../../services/helper/helper.service';

@Component({
    selector: 'app-time-picker',
    templateUrl: './time-picker.component.html',
    styleUrls: ['./time-picker.component.scss']
})
export class TimePickerComponent implements OnInit, OnChanges, OnDestroy {

    @Input() service: ServiceDTO;
    @Input() employeeShift: ShiftDTO;
    @Input() employeeAppointments: EmployeeAppointmentDTO[];

    @Output() timeChange = new EventEmitter();

    startTime: string;
    startTimeSubscription: Subscription;
    @Input() startTimeEvent: Observable<string>;
    @Input() appointmentId?: number;

    timeFormat = 'HH:mm';
    minHour = 8;
    maxHour = 22;
    incrementBy = 30;
    minuteIncrements: number[] = Array(60 / this.incrementBy).fill(0).map((x, y) => x + this.incrementBy * y);

    times: Time[] = [];

    constructor(private helper: HelperService) {
    }

    ngOnInit() {
        this.startTimeSubscription = this.startTimeEvent.subscribe(res => this.startTime = res);
    }

    ngOnChanges() {
        if (this.employeeShift && this.employeeAppointments) {
            this.generateTimes();
        }
    }

    ngOnDestroy() {
        this.startTimeSubscription.unsubscribe();
    }

    generateTimes(): void {

        this.times = [];

        for (let hour = this.minHour; hour <= this.maxHour; hour++) {
            this.minuteIncrements.forEach(minute => {
                const enabled = this.isValid(`${hour}:${minute}`);
                this.times.push({ hour, minute, enabled });
            });
        }

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

    isValid(t: string): boolean {
        const time = moment(t, this.timeFormat);

        if (!this.helper.isWithinShift(time, this.employeeShift)) {
            return false;
        }

        if (this.helper.isDuringAnAppointment(time, this.employeeAppointments, this.appointmentId)) {
            return false;
        }

        return this.helper.canFitServiceIntoEmployeesSchedule(time, this.employeeShift, this.employeeAppointments, this.service.duration, this.appointmentId);

    }

}
