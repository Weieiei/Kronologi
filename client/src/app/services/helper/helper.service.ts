import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { ShiftDTO } from '../../interfaces/shift-dto/shift-dto';
import { Moment } from 'moment';
import { EmployeeAppointmentDTO } from '../../interfaces/appointment/employee-appointment-dto';
import { Time } from '../../interfaces/time';

@Injectable({
    providedIn: 'root'
})
export class HelperService {

    timeFormat = 'HH:mm';
    minHour = 8;
    maxHour = 22;
    incrementBy = 30;
    minuteIncrements: number[] = Array(60 / this.incrementBy).fill(0).map((x, y) => x + this.incrementBy * y);
    times: Time[] = [];

    constructor() {
        for (let hour = this.minHour; hour <= this.maxHour; hour++) {
            this.minuteIncrements.forEach(minute => {
                this.times.push({ hour, minute, enabled: true });
            });
        }
    }

    cloneTimesArray(): Time[] {
        return this.times.map(time => Object.assign({}, time));
    }

    isWithinShift(time: Moment, shift: ShiftDTO): boolean {
        const startTime = moment(shift.startTime, this.timeFormat);
        const endTime = moment(shift.endTime, this.timeFormat);
        return time.isBetween(startTime, endTime) || time.isSame(startTime) || time.isSame(endTime);
    }

    isDuringAnAppointment(time: Moment, employeeAppointments: EmployeeAppointmentDTO[], appointmentId?: number): boolean {
        return employeeAppointments.some(appointment => {
            const startTime = moment(appointment.startTime, this.timeFormat);
            const endTime = moment(appointment.endTime, this.timeFormat);
            return (time.isBetween(startTime, endTime) || time.isSame(startTime)) && appointment.id !== appointmentId;
        });
    }

    canFitServiceIntoEmployeesSchedule(time: Moment, employeeShift: ShiftDTO, employeeAppointments: EmployeeAppointmentDTO[], serviceDuration: number, appointmentId?: number): boolean {

        while (serviceDuration !== 0) {

            // 5 because the greatest common divisor of service durations is 5
            time.add(5, 'm');

            if (!this.isWithinShift(time, employeeShift)) {
                return false;
            }

            if (this.isDuringAnAppointment(time, employeeAppointments, appointmentId)) {
                return false;
            }

            serviceDuration -= 5;
        }

        return true;

    }

    isValid(t: string, employeeShift: ShiftDTO, employeeAppointments: EmployeeAppointmentDTO[], serviceDuration: number, appointmentId?: number): boolean {
        const time = moment(t, this.timeFormat);

        if (!this.isWithinShift(time, employeeShift)) {
            return false;
        }

        if (this.isDuringAnAppointment(time, employeeAppointments, appointmentId)) {
            return false;
        }

        return this.canFitServiceIntoEmployeesSchedule(time, employeeShift, employeeAppointments, serviceDuration, appointmentId);

    }

}
