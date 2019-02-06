import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminEmployeeDTO } from '../../interfaces/employee/admin-employee-dto';
import { AdminEmployeeShiftDTO } from '../../interfaces/shift/admin-employee-shift-dto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

    constructor(private http: HttpClient) {
    }

    public getAllEmployees(): Observable<AdminEmployeeDTO[]> {
        return this.http.get<AdminEmployeeDTO[]>(['api', 'admin', 'employee'].join('/'));
    }

    getEmployeeShifts(employeeId: number): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.get<AdminEmployeeShiftDTO[]>(['api', 'admin', 'employee', employeeId, 'shift'].join('/'));
    }
}
