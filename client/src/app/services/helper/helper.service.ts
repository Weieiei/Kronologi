import { Injectable } from '@angular/core';
import * as moment from 'moment';
import { ShiftDTO } from '../../interfaces/shift-dto/shift-dto';
import { Moment } from 'moment';
import { EmployeeAppointmentDTO } from '../../interfaces/appointment/employee-appointment-dto';

@Injectable({
    providedIn: 'root'
})
export class HelperService {

    timeFormat = 'HH:mm';

    constructor() {
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

}
