import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../../../services/appointment/appointment.service';
import { AppointmentDetailed } from '../../../../models/appointment/AppointmentDetailed';
import { map } from 'rxjs/operators';
import { User } from '../../../../models/user/User';
import { Service } from '../../../../models/service/Service';
import { AppointmentDetail } from 'src/app/interfaces/appointment-detail';
@Component({
  selector: 'app-employee-component',
  templateUrl: './employee-component.component.html',
  styleUrls: ['./employee-component.component.scss']
})
export class EmployeeComponentComponent implements OnInit {

    displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'client', 'employee'];
    appointments: AppointmentDetail[];
    pastAppointments: AppointmentDetail[];

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
