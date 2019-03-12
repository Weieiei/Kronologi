import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { AppointmentDetailed } from '../../../../models/appointment/AppointmentDetailed';
import { map } from 'rxjs/operators';
import { User } from '../../../../models/user/User';
import { Service } from '../../../../models/service/Service';

@Component({
    selector: 'app-admin-appointments',
    templateUrl: './admin-appointments.component.html',
    styleUrls: ['./admin-appointments.component.scss']
})
export class AdminAppointmentsComponent implements OnInit {

    displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee'];
    appointments: AppointmentDetailed[];

    constructor(private appointmentService: AppointmentService) {
    }

    ngOnInit() {
        this.getAllAppointments();
    }

    getAllAppointments(): void {
        this.appointmentService.getAllAppointments().pipe(
            map(data => {
                let i = 0;
                return data.map(a => {
                    const client = a.client;
                    const employee = a.employee;
                    const service = a.service;

                    return new AppointmentDetailed(
                        a.id, a.clientId, a.employeeId, a.serviceId,
                        a.startTime, a.endTime, a.date, a.notes,
                        a.status, a.createdAt, a.updatedAt,
                        new User(
                            client.id, client.firstName, client.lastName,
                            client.email, client.username, client.password,
                            client.userType, client.createdAt, client.updatedAt
                        ),
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
            res => this.appointments = res,
            err => console.log(err)
        );
    }

}
