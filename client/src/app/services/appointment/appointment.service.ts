import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RequestOptions, RequestMethod } from '@angular/http';
import { Observable } from 'rxjs';
import { AppointmentDetailed } from '../../models/appointment/AppointmentDetailed';
import { AppointmentToBook } from '../../models/appointment/AppointmentToBook';
import { Appointment } from 'src/app/interfaces/appointment';
import { CancelAppointmentDTO } from 'src/app/interfaces/cancelAppointmentDTO';

@Injectable({
    providedIn: 'root'
})
export class AppointmentService {

    constructor(private http: HttpClient) {
    }

    public getMyAppointments(): Observable<any> {
        return this.http.get(['api', 'user', 'appointments'].join('/'));
    }

    public getAllAppointments(): Observable<AppointmentDetailed[]> {
        return this.http.get<AppointmentDetailed[]>(['api', 'admin', 'appointments'].join('/'));
    }

    public reserveAppointment(appointment: AppointmentToBook): Observable<any> {
        return this.http.post<AppointmentToBook>(['api', 'user', 'appointments'].join('/'), appointment);
    }

    public cancelAppointments(id:number): Observable<any> {
        return this.http.delete(['api','user','appointments',id].join('/'),{responseType: 'text'})
    }

    public getMyAppointmentsEmployee(): Observable<Appointment[]> {
        return this.http.get<Appointment[]>(['api', 'employee', 'appointments'].join('/'));
    }

    public cancelAppointmentsEmployee(appointment:CancelAppointmentDTO): Observable<any>{        
        return this.http.post<CancelAppointmentDTO>(['api','employee','appointments','cancel'].join('/'),  appointment);
    }

    public cancelAppointmentReason(id:any): Observable<any>{
        return this.http.get(['api', 'appointments','cancel',id].join('/'));
    }
}
