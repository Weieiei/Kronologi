import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../services/appointment/appointment.service';

@Component({
  selector: 'app-my-appointments',
  templateUrl: './my-appointments.component.html',
  styleUrls: ['./my-appointments.component.scss']
})
export class MyAppointmentsComponent implements OnInit {

  displayedColumns: string[] = ['service', 'day', 'start', 'end', 'duration', 'notes'];
  appointments: any[] = [];

  constructor(private appointmentService: AppointmentService) { }

  ngOnInit() {
    this.getMyAppointments();
  }

  getMyAppointments(): void {
    this.appointmentService.getMyAppointments().subscribe(
      res => this.appointments = res['appointments'],
      err => console.log(err)
    );
  }

}
