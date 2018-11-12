
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Service } from 'src/app/models/service/service';
import { AuthService } from 'src/app/services/auth.service';
import { Appointment } from 'src/app/models/appointment/appointment';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Time } from '@angular/common';

@Component({
  selector: 'app-reserve',
  templateUrl: './reserve.component.html',
  styleUrls: ['./reserve.component.css']
})

export class ReserveComponent implements OnInit {
  service: Service = null;
  selectedService: Service;
  service_options: Array<Service>;
  today: number = Date.now();
  startDate: Date;
  startTime: Time;
  dateFilter = (date: Date) => date.getDate() <= this.today;

  constructor(private authService: AuthService,
    private router: Router, private http: HttpClient) { }

  getServiceJson() {
    return this.http.get<any>('api/services');
  }


  ngOnInit() {
    console.log(this.today);
    this.getServiceJson().subscribe((object: Array<Service>) => {
      this.service_options = object;
      console.log(object);
      console.log(this.service_options[0]);
    });

  }
  reserve_service() {
    let appointment = new Appointment();
    appointment.start_time = this.startTime;
    appointment.end_time = this.startTime // add the minutes
    appointment.date = this.startDate
    appointment.service_id = this.selectedService.id
    // appointment.user_id = user.id
    this.addAppointment(appointment);





  }

  addAppointment(appointment: Appointment): Observable<Appointment> {
    return this.http.post<Appointment>(['api', 'appointment'].join('/'), { appointment });
  }

}
