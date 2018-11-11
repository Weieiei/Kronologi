import { Component, OnInit } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment.service';
import { Appointment } from 'src/app/models/appointment/appointment';

@Component({
  selector: 'app-my-appointments',
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.css']
})
export class MyAppointmentsComponent implements OnInit {

  appointments: Appointment[];

  constructor(private appointmentService: AppointmentService) { }

  ngOnInit() {
    this.getMyAppointments();
  }

  getMyAppointments(): void {
    this.appointmentService.getMyAppointments().subscribe(
      res => this.appointments = res,
      err => console.log(err)
    )
  }

}
