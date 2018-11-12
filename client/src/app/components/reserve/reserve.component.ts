
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Service } from 'src/app/models/service/service';
import { AuthService } from 'src/app/services/auth/auth.service';
import { Appointment } from 'src/app/models/appointment/appointment';
import { User } from 'src/app/models/user/user';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-reserve',
  templateUrl: './reserve.component.html',
  styleUrls: ['./reserve.component.css']
})

export class ReserveComponent implements OnInit {
  appointment: Appointment;
  service: Service = null;
  selectedOption: string;
  service_options: Array<Service>;
  today: number = Date.now();
  dateFilter = (date: Date) => date.getDate() <= this.today;


  print() {
    this.service.name = this.selectedOption;
  }

  constructor(private authService: AuthService,
    private router: Router, private http: HttpClient) { }

  getServiceJson() {
    return this.http.get<any>('api/services');
  }


  ngOnInit() {
    this.appointment = new Appointment();
    console.log(this.today);
    this.getServiceJson().subscribe((object: Array<Service>) => {
      this.service_options = object;
      console.log(object);
      console.log(this.service_options[0]);
    });

  }
  reserve_service() {
    // TODO
  }

}
