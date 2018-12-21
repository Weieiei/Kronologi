import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AppointmentDetailed } from '../../models/appointment/AppointmentDetailed';
import { AppointmentToBook } from '../../models/appointment/AppointmentToBook';
import { MyAppointment } from '../../models/appointment/MyAppointment';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  public getMyAppointments(): Observable<MyAppointment[]> {
    return this.http.get<MyAppointment[]>(['api', 'user', 'appointments'].join('/'));
  }

  public getAllAppointments(): Observable<AppointmentDetailed[]> {
    return this.http.get<AppointmentDetailed[]>(['api', 'admin', 'appointments'].join('/'));
  }

  public reserveAppointment(appointment: AppointmentToBook): Observable<any> {
    return this.http.post<AppointmentToBook>(['api', 'user', 'appointments'].join('/'), appointment);
  }
}
