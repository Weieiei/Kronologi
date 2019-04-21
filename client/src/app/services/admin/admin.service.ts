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

    public getAllEmployees(businessId: number): Observable<AdminEmployeeDTO[]> {
        return this.http.get<AdminEmployeeDTO[]>(['api', 'business',  businessId, 'admin','employees'].join('/'));
    }

    getEmployee(businessId:number, employeeId: number): Observable<AdminEmployeeDTO> {
        return this.http.get<AdminEmployeeDTO>(['api', 'business', businessId, 'admin', 'employee', employeeId].join('/'));
    }

    getEmployeeShifts(businessId:number, employeeId: number): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.get<AdminEmployeeShiftDTO[]>(['api', 'business', businessId, 'admin', 'employee', employeeId, 'shift'].join('/'));
    }

    addShift(businessId: number, employeeId: number, payload: NewShiftDTO): Observable<AdminEmployeeShiftDTO> {
        return this.http.post<AdminEmployeeShiftDTO>(['api', 'business', businessId.toString(), 'admin', 'employee', employeeId, 'shift'].join('/'), payload);
    }

     addShiftList(businessId:number, employeeId: number, payload: Array<NewShiftDTO>): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.post<AdminEmployeeShiftDTO[]>(['api', 'business', businessId, 'admin', 'employee', employeeId, 'shift-list'].join('/'), payload);
    }

    deleteShift(businessId: number, shiftId: number): Observable<any> {
        return this.http.delete<any>(['api', 'business', businessId, 'admin', 'employee', 'shift', shiftId].join('/'));
    }
}
