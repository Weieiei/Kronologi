import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../services/appointment/appointment.service';
import { MyAppointment } from '../../models/appointment/MyAppointment';

@Component({
  selector: 'app-my-appointments',
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.scss']
})
export class MyAppointmentsComponent implements OnInit {

  displayedColumns: string[] = ['service', 'employee', 'day', 'start', 'end', 'duration', 'notes'];
  appointments: MyAppointment[] = [];

  constructor(private appointmentService: AppointmentService) { }

  ngOnInit() {
    this.getMyAppointments();
  }

  getMyAppointments(): void {
    this.appointmentService.getMyAppointments().subscribe(
      res => this.appointments = res,
      err => console.log(err)
    );
  }

}
