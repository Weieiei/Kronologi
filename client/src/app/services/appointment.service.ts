import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  constructor(private http: HttpClient) { }

  getMyAppointments(): Observable<any[]> {
    return this.http.get<any[]>(['api', 'appointments', 'user'].join('/'));
  }

}
