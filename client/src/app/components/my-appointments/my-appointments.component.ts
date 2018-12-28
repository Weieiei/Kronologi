import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../services/appointment/appointment.service';
import { MyAppointment } from '../../models/appointment/MyAppointment';
import { AppointmentStatus } from '../../models/appointment/AppointmentStatus';
import { map } from 'rxjs/operators';
import { User } from '../../models/user/User';
import { Service } from '../../models/service/Service';

@Component({
    selector: 'app-my-appointments',
    templateUrl: './my-appointments.component.html',
    styleUrls: ['./my-appointments.component.scss']
})
export class MyAppointmentsComponent implements OnInit {

    displayedColumns: string[] = ['service', 'employee', 'day', 'start', 'end', 'duration', 'notes', 'status'];
    appointments: MyAppointment[];
    AppointmentStatus = AppointmentStatus;

    constructor(private appointmentService: AppointmentService) {
    }

    ngOnInit() {
        this.getMyAppointments();
    }

    getMyAppointments(): void {
        this.appointmentService.getMyAppointments().pipe(
            map(data => {

                this.appointments = data.map(a => {

                    const employee = a.employee;
                    const service = a.service;

                    return new MyAppointment(
                        a.id, a.clientId, a.employeeId, a.serviceId,
                        a.startTime, a.endTime, a.notes, a.status,
                        a.createdAt, a.updatedAt,
                        new User(
                            employee.id, employee.firstName, employee.lastName,
                            employee.email, employee.username, employee.password,
                            employee.userType, employee.createdAt, employee.updatedAt
                        ),
                        new Service(
                            service.id, service.name, service.duration,
                            service.createdAt, service.updatedAt
                        )
                    );

                });

            })
        ).subscribe(
            res => void 0,
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
