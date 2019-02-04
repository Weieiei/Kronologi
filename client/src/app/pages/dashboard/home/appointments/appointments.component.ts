import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { MatDialog } from '@angular/material';

@Component({
    selector: 'app-appointments',
    templateUrl: './appointments.component.html',
    styleUrls: ['./appointments.component.scss']
})
export class AppointmentsComponent implements OnInit {

    upcomingAppointments;
    pastAppointments;

    upcomingMessageMapping: { [k: string]: string } = {
        '=0': 'No Upcoming Appointments',
        '=1': 'One Upcoming Appointment',
        'other': '# Upcoming Appointments'
    };

    historyMessageMapping: { [k: string]: string } = {
        '=0': 'No Previous Appointments',
        '=1': 'One Previous Appointment',
        'other': '# Previous Appointments'
    };

    constructor(private dialog: MatDialog, private appointmentService: AppointmentService) {
        this.upcomingAppointments = [];
        this.pastAppointments = [];
    }

    ngOnInit() {
        this.dialog.afterAllClosed
        .subscribe(() => {
            this.upcomingAppointments = [];
            this.pastAppointments = [];
            this.getMyAppointments();
        });
    }

    getMyAppointments(): void {
        this.appointmentService.getMyAppointments().subscribe(
            res => {
                const now = new Date();
                
                for (const appointment of res) {
                    const appointmentStart = new Date(appointment.date + ' ' + appointment.startTime);
                    
                    if (now <= appointmentStart) {
                        this.upcomingAppointments.push(appointment);
                    } else {
                        this.pastAppointments.push(appointment);
                    }
                }
            },
            err => console.log(err)
        );
    }
}
