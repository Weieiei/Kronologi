import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookAppointmentDTO } from '../../interfaces/appointment/book-appointment-dto';
import { UserAppointmentDTO } from '../../interfaces/appointment/user-appointment-dto';

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
}
