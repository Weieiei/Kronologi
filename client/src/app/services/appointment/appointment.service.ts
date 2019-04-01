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
        return this.http.get<AppointmentDetailed[]>(['api','business', '1', 'admin' , 'appointments'].join('/'));
    }

    public getMyAppointments(): Observable<UserAppointmentDTO[]> {
        return this.http.get<UserAppointmentDTO[]>(['api', 'business', '1', 'user', 'appointments',].join('/'));
    }

    public getAppointmentById(id: number): Observable<UserAppointmentDTO> {
        return this.http.get<UserAppointmentDTO>(['api', 'business','1', 'user', 'appointments', id].join('/'));
    }

    public bookAppointment(payload: BookAppointmentDTO): Observable<any> {
        return this.http.post<any>(['api', 'business','1','appointments','appointments'].join('/'), payload);
    }

    public updateAppointment(id: number, payload: BookAppointmentDTO): Observable<any> {
        return this.http.put<any>(['api','business','1','appointments', id].join('/'), payload);
    }

    public cancelAppointments(id:number, payload: CancelAppointmentDTO): Observable<any> {
        return this.http.post<CancelAppointmentDTO>(['api','business','1','user','appointments',id].join('/'),payload)
    }

    public getMyAppointmentsEmployee(): Observable<Appointment[]> {
        return this.http.get<Appointment[]>(['api', 'business','1','employee','appointments'].join('/'));
    }

    public cancelAppointmentsEmployee(appointment:CancelAppointmentDTO): Observable<any>{        
        return this.http.post<CancelAppointmentDTO>(['api','business','1','employee','appointments','cancel'].join('/'),  appointment);
    }

    public cancelAppointmentReason(id:any): Observable<any>{
        return this.http.get(['api', 'business','1','appointments','cancel',id].join('/'));
    }

    public getAvailabilitiesForService(serviceId:number ): Observable<any>{
        return this.http.get(['api', 'business','1','availabilities',serviceId].join('/'));
    }

    public googleLogin(): Observable<any>{
        return this.http.get(['external', 'google','login','google'].join('/'));
    }


}
