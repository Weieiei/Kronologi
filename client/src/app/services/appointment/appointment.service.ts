import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RequestOptions, RequestMethod } from '@angular/http';
import { Observable } from 'rxjs';
import { BookAppointmentDTO } from '../../interfaces/appointment/book-appointment-dto';
import { UserAppointmentDTO } from '../../interfaces/appointment/user-appointment-dto';
import { Appointment } from 'src/app/interfaces/appointment';
import { CancelAppointmentDTO } from 'src/app/interfaces/cancelAppointmentDTO';
import { AppointmentDetailed } from '../../models/appointment/AppointmentDetailed';

@Injectable({
    providedIn: 'root'
})
//TODO FIX TO GET REAL BUSINESSID
export class AppointmentService {

    constructor(private http: HttpClient) {
    }

    public getAllAppointments(): Observable<AppointmentDetailed[]> {
        return this.http.get<AppointmentDetailed[]>(['api','business','admin', '1', 'appointments'].join('/'));
    }

    public getMyAppointments(): Observable<UserAppointmentDTO[]> {
        return this.http.get<UserAppointmentDTO[]>(['api', 'user', 'business', '1','appointments',].join('/'));
    }

    public getAppointmentById(id: number): Observable<UserAppointmentDTO> {
        return this.http.get<UserAppointmentDTO>(['api', 'user', 'business','1', 'appointments', id].join('/'));
    }

    public bookAppointment(payload: BookAppointmentDTO): Observable<any> {
        return this.http.post<any>(['api', 'business','appointments','1','appointments'].join('/'), payload);
    }

    public updateAppointment(id: number, payload: BookAppointmentDTO): Observable<any> {
        return this.http.put<any>(['api','business','1','appointments', id].join('/'), payload);
    }

    public cancelAppointments(id:number, payload: CancelAppointmentDTO): Observable<any> {
        return this.http.post<CancelAppointmentDTO>(['api','user','business','1','appointments',id].join('/'),payload)
    }

    public getMyAppointmentsEmployee(): Observable<Appointment[]> {
        return this.http.get<Appointment[]>(['api', 'business','employee','1','appointments'].join('/'));
    }

    public cancelAppointmentsEmployee(appointment:CancelAppointmentDTO): Observable<any>{        
        return this.http.post<CancelAppointmentDTO>(['api','business','employee','1','appointments','cancel'].join('/'),  appointment);
    }

    public cancelAppointmentReason(id:any): Observable<any>{
        return this.http.get(['api', 'business','appointments','1','cancel',id].join('/'));
    }

    public googleLogin(): Observable<any>{
        return this.http.get(['external', 'google','login','google'].join('/'));
    }
}
