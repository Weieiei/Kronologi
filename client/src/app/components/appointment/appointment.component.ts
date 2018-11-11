import { Component, OnInit, Input } from '@angular/core';
import { Appointment } from 'src/app/models/appointment/appointment';

@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.css']
})
export class AppointmentComponent implements OnInit {

  @Input() appointment: Appointment;

  constructor() { }

  ngOnInit() {
  }

}
