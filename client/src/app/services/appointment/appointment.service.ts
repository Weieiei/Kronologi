import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { AppointmentDetailed } from '../../models/appointment/AppointmentDetailed';
import { AppointmentToBook } from '../../models/appointment/AppointmentToBook';
import { AppointmentDetail } from 'src/app/interfaces/appointment-detail';
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

    public getMyAppointmentsEmployee(): Observable<AppointmentDetail[]> {
        return this.http.get<AppointmentDetail[]>(['api', 'appointments', 'current_employee'].join('/'));
    }
}
