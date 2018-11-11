
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Service } from 'src/app/models/service/service';
import { AuthService } from 'src/app/services/auth.service';
import { Appointment } from 'src/app/models/appointment/appointment';
import { User } from 'src/app/models/user/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reserve',
  templateUrl: './reserve.component.html',
  styleUrls: ['./reserve.component.css']
})
export class ReserveComponent implements OnInit {
  appointment: Appointment;
  service: Service;
  selectedOption: string;
  service_options = [
    { name: 'option1', value: 1 },
    { name: 'option2', value: 2 } // using getting to get a list of services
  ];
  print() {
    this.service.name = this.selectedOption;
  }

  constructor(private authService: AuthService,
    private router: Router) { }

  ngOnInit() {
    this.appointment = new Appointment();
  }
  reserve_service() {
    // TODO
  }

}
