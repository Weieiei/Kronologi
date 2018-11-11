import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment } from '../models/appointment/appointment';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  getMyAppointments(): Observable<Appointment[]> {
    return this.http.get<Appointment[]>(['api', 'appointments', 'user'].join('/'));
  }

}
