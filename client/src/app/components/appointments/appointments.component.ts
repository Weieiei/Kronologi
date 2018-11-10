import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {HttpClient} from '@angular/common/http';

export interface Appointment {
  service: string;
  date: string;
  time: string;
  duration: string;
  user: string;
}

const APPOINTMENT_DATA: Appointment[] = [
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
  {service: 'A service', date: '2018-11-10', time: '5:00 PM', duration: '1 hour', user: 'A user'},
];

@Component({
  selector: 'app-appointments',
  templateUrl: './appointments.component.html',
  styleUrls: ['./appointments.component.css']
})
export class AppointmentsComponent implements OnInit {

  displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'user'];
  dataSource = APPOINTMENT_DATA;

  constructor(private authService: AuthService, private httpService: HttpClient) { }

  ngOnInit() {
  }

  clicked() {
    console.log('clicked');
    this.httpService.post('api/appointments', { token: this.authService.getToken()}).subscribe(res => {
      console.log(res);
    });
  }
}
