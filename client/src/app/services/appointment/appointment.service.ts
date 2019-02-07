import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RequestOptions, RequestMethod } from '@angular/http';
import { Observable } from 'rxjs';
import { BookAppointmentDTO } from '../../interfaces/appointment/book-appointment-dto';
import { UserAppointmentDTO } from '../../interfaces/appointment/user-appointment-dto';
import { Appointment } from 'src/app/interfaces/appointment';
import { CancelAppointmentDTO } from 'src/app/interfaces/cancelAppointmentDTO';

@Injectable({
    providedIn: 'root'
})
export class AppointmentService {

    constructor(private http: HttpClient) {
    }

    public getMyAppointments(): Observable<UserAppointmentDTO[]> {
        return this.http.get<UserAppointmentDTO[]>(['api', 'user', 'appointments'].join('/'));
    }

    public getAppointmentById(id: number): Observable<UserAppointmentDTO> {
        return this.http.get<UserAppointmentDTO>(['api', 'user', 'appointments', id].join('/'));
    }

    public bookAppointment(payload: BookAppointmentDTO): Observable<any> {
        return this.http.post<any>(['api', 'appointments'].join('/'), payload);
    }

    public updateAppointment(id: number, payload: BookAppointmentDTO): Observable<any> {
        return this.http.put<any>(['api', 'appointments', id].join('/'), payload);
    }

    public cancelAppointments(id:number, payload: CancelAppointmentDTO): Observable<any> {
        return this.http.post<CancelAppointmentDTO>(['api','user','appointments',id].join('/'),payload)
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
