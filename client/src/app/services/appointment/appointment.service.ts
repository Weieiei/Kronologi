import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment } from '../../interfaces/appointment';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  public getMyAppointments(): Observable<any[]> {
    return this.http.get<any[]>(['api', 'user', 'appointments'].join('/'));
  }

  public reserveAppointment(data: Appointment): Observable<any> {
    return this.http.post<any>(['api', 'user', 'appointments'].join('/'), data);
  }
}
