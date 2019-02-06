import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { EmployeeDTO } from '../../interfaces/user/employee-dto';
import { ShiftDTO } from '../../interfaces/shift-dto/shift-dto';
import { EmployeeAppointmentDTO } from '../../interfaces/appointment/employee-appointment-dto';

@Injectable({
    providedIn: 'root'
})
export class EmployeeService {

    constructor(private http: HttpClient) {
    }

    public getAllEmployees(): Observable<any[]> {
        return this.http.get<any[]>(['api', 'employees'].join('/'));
    }

    public getAvailableEmployeesByServiceAndByDate(serviceId: number, date: string): Observable<EmployeeDTO[]> {
        return this.http.get<EmployeeDTO[]>(['api', 'appointments', 'employees', serviceId].join('/'), { params: { date } });
    }

    public getSelectedEmployeesShiftByDate(employeeId: number, date: string): Observable<ShiftDTO> {
        return this.http.get<ShiftDTO>(['api', 'appointments', 'employee', employeeId, 'shift'].join('/'), { params: { date } });
    }

    public getSelectedEmployeesAppointmentsByDate(employeeId: number, date: string): Observable<EmployeeAppointmentDTO[]> {
        return this.http.get<EmployeeAppointmentDTO[]>(['api', 'appointments', 'employee', employeeId, 'appointments'].join('/'), { params: { date } });
    }

    getAvailableEmployeesShiftsByDate(date: string): Observable<ShiftDTO[]> {
        return this.http.get<ShiftDTO[]>(['api', 'appointments', 'employee', 'shifts'].join('/'), { params: { date } });
    }

    getAvailableEmployeesAppointmentsByDate(date: string): Observable<EmployeeAppointmentDTO[]> {
        return this.http.get<EmployeeAppointmentDTO[]>(['api', 'appointments', 'employee', 'appointments'].join('/'), { params: { date } });
    }
}
