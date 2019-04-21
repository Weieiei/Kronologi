import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { EmployeeDTO } from '../../interfaces/employee/employee-dto';
import { ShiftDTO } from '../../interfaces/shift/shift-dto';
import { EmployeeAppointmentDTO } from '../../interfaces/appointment/employee-appointment-dto';

@Injectable({
    providedIn: 'root'
})
export class EmployeeService {

    constructor(private http: HttpClient) {
    }

    //Can't find the route in the srever as of march 11th 2019
    public getAllEmployees(): Observable<any[]> {
        return this.http.get<any[]>(['api','business', 'employees'].join('/'));
    }

    
    public getAvailableEmployeesByServiceAndByDate(serviceId: number, date: string): Observable<EmployeeDTO[]> {
        return this.http.get<EmployeeDTO[]>(['api','business', 'appointments','1', 'employee', serviceId].join('/'), { params: { date } });
    }

    public getSelectedEmployeesShiftByDate(employeeId: number, date: string): Observable<ShiftDTO> {
        return this.http.get<ShiftDTO>(['api', ,'business', 'appointments','1', 'employee', employeeId, 'shift'].join('/'), { params: { date } });
    }

    public getSelectedEmployeesAppointmentsByDate(employeeId: number, date: string): Observable<EmployeeAppointmentDTO[]> {
        return this.http.get<EmployeeAppointmentDTO[]>(['api', 'business','appointments','1', 'employee', employeeId, 'appointments'].join('/'), { params: { date } });
    }

    getAvailableEmployeesShiftsByDate(date: string): Observable<ShiftDTO[]> {
        return this.http.get<ShiftDTO[]>(['api','business', '1', 'employee', 'shifts'].join('/'), { params: { date } });
    }

    getAvailableEmployeesAppointmentsByDate(date: string): Observable<EmployeeAppointmentDTO[]> {
        return this.http.get<EmployeeAppointmentDTO[]>(['api', 'business','appointments','1', 'employee', 'appointments'].join('/'), { params: { date } });
    }
}
