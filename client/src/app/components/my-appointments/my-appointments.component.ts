import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../services/appointment/appointment.service';
import { MyAppointment } from '../../models/appointment/MyAppointment';
import { AppointmentStatus } from '../../models/appointment/AppointmentStatus';

@Component({
    selector: 'app-my-appointments',
    templateUrl: './my-appointments.component.html',
    styleUrls: ['./my-appointments.component.scss']
})
export class MyAppointmentsComponent implements OnInit {

    displayedColumns: string[] = ['service', 'employee', 'day', 'start', 'end', 'duration', 'notes', 'status'];
    appointments: MyAppointment[] = [];
    AppointmentStatus = AppointmentStatus;

    constructor(private appointmentService: AppointmentService) {
    }

    ngOnInit() {
        this.getMyAppointments();
    }

    getMyAppointments(): void {
        this.appointmentService.getMyAppointments().subscribe(
            res => this.appointments = res,
            err => console.log(err)
        );
    }

    cancelAppointment(appointmentId: number): void {

        const cancel = confirm('Do you want to cancel this appointment?');
        if (cancel) {

            this.appointmentService.cancelAppointment(appointmentId).subscribe(
                res => this.appointments.find(appointment => appointment.id === appointmentId).status = AppointmentStatus.cancelled,
                err => alert(err.error.message)
            );

        }

    }

}
