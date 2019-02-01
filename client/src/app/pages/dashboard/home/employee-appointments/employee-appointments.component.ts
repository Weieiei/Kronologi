import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { Appointment } from 'src/app/interfaces/appointment';

@Component({
    selector: 'app-employee-appointments',
    templateUrl: './employee-appointments.component.html',
    styleUrls: ['./employee-appointments.component.scss']
})
export class EmployeeAppointmentsComponent implements OnInit {

    displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee'];
    appointments: Appointment[];
    pastAppointments: Appointment[];

    constructor(private appointmentService: AppointmentService) {
        this.appointments = [];
        this.pastAppointments = [];
    }

    ngOnInit() {
        this.getAllAppointments();
    }

    getAllAppointments(): void {
        this.appointmentService.getMyAppointmentsEmployee().subscribe(
            res => {

                const now = new Date();

                for (const appointment of res) {
                    const appointmentStart = new Date(appointment.date + ' ' + appointment.startTime);


                    if (now <= appointmentStart) {
                        this.appointments.push(appointment);
                    } else {
                        this.pastAppointments.push(appointment);

                    }
                }
            },
            err => console.log(err)
        );
    }

}
