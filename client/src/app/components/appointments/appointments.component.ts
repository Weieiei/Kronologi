import { Component, OnInit } from '@angular/core';
import {AppointmentService} from "../../services/appointment/appointment.service";
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

  constructor(private appointmentService: AppointmentService) { }

  ngOnInit() {
    this.getAllAppointments();
  }

  getAllAppointments(): void {
    this.appointmentService.getAllAppointments().subscribe(
      res => {this.rawData = res['appointments'];
        this.populateServiceNames();},
      err => console.log(err)
    );
  }

  populateServiceNames() {
    const dataSourceParsed: Appointment[] = [];
    this.rawData.forEach(item => {
      const appointment: Appointment = {
        service: item.name,
        date: this.getDate(item.start_time),
        time: this.getTime(item.start_time),
        duration: item.duration + " min.",
        user: item.first_name + " " + item.last_name,
      };
      dataSourceParsed.push(appointment);
    });
    this.dataSource = dataSourceParsed;
  }

  getDate(date: any) {
    const processedDate = new Date(date);
    return moment(processedDate).format('YYYY-MM-DD');
  }

  getTime(date: any) {
    const processedDate = new Date(date);
    return moment(processedDate).format('hh:mm A');
  }
}
