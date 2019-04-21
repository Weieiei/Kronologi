import { Component, OnInit , Input} from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { MatDialog } from '@angular/material';
import { UserAppointmentDTO } from '../../../../interfaces/appointment/user-appointment-dto';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
    selector: 'app-appointments',
    templateUrl: './appointments.component.html',
    styleUrls: ['./appointments.component.scss']
})
export class AppointmentsComponent implements OnInit {
    @Input()  businessId;
    upcomingAppointments: UserAppointmentDTO[];
    pastAppointments: UserAppointmentDTO[];

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

    constructor(private dialog: MatDialog, private appointmentService: AppointmentService, private authService : AuthService) {
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
        this.appointmentService.getMyAppointments(this.businessId).subscribe(
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
