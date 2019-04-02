import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminEmployeeDTO } from '../../interfaces/employee/admin-employee-dto';
import { AdminEmployeeShiftDTO } from '../../interfaces/shift/admin-employee-shift-dto';
import { NewShiftDTO } from '../../interfaces/shift/new-shift-dto';

@Injectable({
    providedIn: 'root'
})
export class AdminService {

    constructor(private http: HttpClient) {
    }

    public getAllEmployees(): Observable<AdminEmployeeDTO[]> {
        return this.http.get<AdminEmployeeDTO[]>(['api', 'business','admin','1', 'employees'].join('/'));
    }

    getEmployee(employeeId: number): Observable<AdminEmployeeDTO> {
        return this.http.get<AdminEmployeeDTO>(['api', 'business','admin', '1','employee', employeeId].join('/'));
    }

    getEmployeeShifts(employeeId: number): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.get<AdminEmployeeShiftDTO[]>(['api', 'business','admin', '1','employee', employeeId, 'shift'].join('/'));
    }

    addShift(employeeId: number, payload: NewShiftDTO): Observable<AdminEmployeeShiftDTO> {
        return this.http.post<AdminEmployeeShiftDTO>(['api', 'business','admin', '1','employee', employeeId, 'shift'].join('/'), payload);
    }
    addShiftList(employeeId: number, payload: NewShiftDTO[]): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.post<AdminEmployeeShiftDTO[]>(['api', 'business','admin', '1', 'employee', employeeId, 'shift-list'].join('/'), payload);
    }

    deleteShift(shiftId: number): Observable<any> {
        return this.http.delete<any>(['api', 'business','admin', '1', 'employee', 'shift', shiftId].join('/'));
    }
}
