import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { EmployeeDTO } from '../../interfaces/user/employee-dto';

@Injectable({
    providedIn: 'root'
})
export class EmployeeService {

    constructor(private http: HttpClient) {
    }

    public getAllEmployees(): Observable<any[]> {
        return this.http.get<any[]>(['api', 'employees'].join('/'));
    }

    public getAvailableEmployees(date: string): Observable<EmployeeDTO[]> {
        return this.http.get<EmployeeDTO[]>(['api', 'appointments', 'employees'].join('/'), { params: { date } });
    }
}
