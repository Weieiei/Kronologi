import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import {HttpClient} from '@angular/common/http';
import {AppointmentService} from "../../services/appointment/appointment.service";
import {Service} from "../../interfaces/service";
import {ServicesService} from "../../services/services/services.service";
import * as moment from "moment";

export interface Appointment {
  service: string;
  date: string;
  time: string;
  duration: string;
  user: string;
}

@Component({
  selector: 'app-appointments',
  templateUrl: './appointments.component.html',
  styleUrls: ['./appointments.component.css']
})
export class AppointmentsComponent implements OnInit {

  displayedColumns: string[] = ['service', 'date', 'time', 'duration', 'user'];
  dataSource: Appointment[] = [];
  rawData: any[];
  services: any[];



  constructor(private authService: AuthService,
              private httpService: HttpClient,
              private appointmentService: AppointmentService,
              private servicesService: ServicesService) { }

  ngOnInit() {
    this.getServices();
    this.getAllAppointments();
  }

  getAllAppointments(): void {
    this.appointmentService.getAllAppointments().subscribe(
      res => {this.rawData = res;
        this.populateServiceNames();},
      err => console.log(err)
    );
  }

  getServices() {
    this.servicesService.getServices().subscribe(
      res => this.services = res,
      err => console.log(err)
    )
  }

  populateServiceNames() {
    const dataSourceParsed: Appointment[] = [];
    this.rawData.forEach(item => {
      const appointment: Appointment = {
        service: this.findServiceById(item.service_id).name,
        date: this.getDate(item.start_time),
        time: this.getTime(item.start_time),
        duration: this.getDuration(item.start_time, item.end_time),
        user: item.user_id.toString(),
      };
      dataSourceParsed.push(appointment);
    });
    this.dataSource = dataSourceParsed;
  }

  getDuration(startTime: Date, endTime: Date) {
    const start: Date = new Date(startTime);
    const end: Date = new Date(endTime);
    // @ts-ignore
    const duration = Math.abs(end - start) / 36e5 * 60;
    return duration.toFixed(0) + " min.";
  }

  getDate(date: any) {
    const processedDate = new Date(date);
    return moment(processedDate).format('YYYY-MM-DD');
  }

  getTime(date: any) {
    const processedDate = new Date(date);
    return moment(processedDate).format('hh:mm A');
  }

  private findServiceById(id: number): Service {
    return this.services.find(service => service.id == id);
  }
}
