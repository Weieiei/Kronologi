import { Component, OnInit } from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-appointments',
  templateUrl: './appointments.component.html',
  styleUrls: ['./appointments.component.css']
})
export class AppointmentsComponent implements OnInit {

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
