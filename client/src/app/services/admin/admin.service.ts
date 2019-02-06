import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdminEmployeeDTO } from '../../interfaces/employee/admin-employee-dto';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

    constructor(private http: HttpClient) {
    }

    public getAllEmployees(): Observable<AdminEmployeeDTO[]> {
        return this.http.get<AdminEmployeeDTO[]>(['api', 'admin', 'employee'].join('/'));
    }
}
