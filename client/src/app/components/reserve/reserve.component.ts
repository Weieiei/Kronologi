import { Component, OnInit } from '@angular/core';
import { AppointmentService } from 'src/app/services/appointment/appointment.service';
import { ServicesService } from 'src/app/services/services/services.service';
import { Service } from 'src/app/interfaces/service';
import * as moment from 'moment';
import { Appointment } from 'src/app/models/appointment/appointment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-reserve',
  templateUrl: './reserve.component.html',
  styleUrls: ['./reserve.component.scss']
})

export class ReserveComponent implements OnInit {

  services: any[];

  appointment: Appointment;
  date: string;
  startTime: string;
  endTime: string;

  constructor(
    private appointmentService: AppointmentService,
    private servicesService: ServicesService,
    private router: Router
  ) { }

  ngOnInit() {
    this.appointment = new Appointment();
    this.getServices();
  }

  getServices() {
    this.servicesService.getServices().subscribe(
      res => this.services = res,
      err => console.log(err)
    )
  }

  private findServiceById(id: number): Service {
    return this.services.find(service => service.id == id);
  }

  updateEndTime() {
    if (this.appointment.service_id != undefined && this.startTime != undefined) {
      const service: Service = this.findServiceById(this.appointment.service_id);
      const date = moment('2012-12-12 ' + this.startTime).add(service.duration, 'm');
      this.endTime = date.format('HH:mm:ss');
    }
  }

  makeAppointment(): void {
    let date = moment(this.date).format('YYYY-MM-DD');
    this.appointment.start_time = moment(date + ' ' + this.startTime).format('YYYY-MM-DD HH:mm:ss');
    this.appointmentService.reserveAppointment(this.appointment).subscribe(
      res => this.router.navigate(['/my/appts']),
      err => console.log(err)
    )
  }

}
