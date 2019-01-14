import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';

@Component({
    selector: 'app-appointments',
    templateUrl: './appointments.component.html',
    styleUrls: ['./appointments.component.scss']
})
export class AppointmentsComponent implements OnInit {

    upcomingAppointments = [];
    pastAppointments = [];

    upcomingMessageMapping: { [k: string]: string } = {
        '=0': 'No Upcoming Appointments',
        '=1': 'One Upcoming Appointment',
        'other': '# Upcoming Appointments'
    };

    historyMessageMapping: { [k: string]: string }  = {
        '=0': 'No Previous Appointments',
        '=1': 'One Previous Appointment',
        'other': '# Previous Appointments'
    };

    constructor(private appointmentService: AppointmentService) {
    }

    ngOnInit() {
        this.getMyAppointments();
        this.getMyPastAppointments();
    }

    getMyAppointments(): void {
        this.appointmentService.getMyAppointments().subscribe(
            res => {
                const now = new Date();

                for (const appointment of res) {
                    const appointmentStart = new Date(appointment.date + ' ' + appointment.startTime);

                    if (now <= appointmentStart) {
                        this.upcomingAppointments.push(appointment);
                    }
                }
            },
            err => console.log(err)
        );
    }

    getMyPastAppointments(): void {
        this.appointmentService.getMyAppointments().subscribe(
            res => {
                const now = new Date();

                for (const appointment of res) {
                    const appointmentStart = new Date(appointment.date + ' ' + appointment.startTime);

                    if (now > appointmentStart) {
                        this.pastAppointments.push(appointment);
                    }
                }
            },
            err => console.log(err)
        );
    }

}
