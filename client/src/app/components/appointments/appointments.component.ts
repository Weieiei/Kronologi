import { Component, OnInit } from '@angular/core';
import { AppointmentService } from '../../services/appointment/appointment.service';

@Component({
  selector: 'app-appointments',
  templateUrl: './appointments.component.html',
  styleUrls: ['./appointments.component.scss']
})
export class AppointmentsComponent implements OnInit {

  displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'user'];
  appointments: any[];

  constructor(private appointmentService: AppointmentService) { }

  ngOnInit() {
    this.getAllAppointments();
  }

  getAllAppointments(): void {
    this.appointmentService.getAllAppointments().subscribe(
      res => this.appointments = res['appointments'],
      err => console.log(err)
    );
  }

}
