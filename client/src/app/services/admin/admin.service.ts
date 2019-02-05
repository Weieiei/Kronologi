import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AdminService {

    constructor(private http: HttpClient) {
    }

    public getEmployeeShifts(): Observable<any> {
        return this.http.get(['api', 'admin', 'employee'].join('/'));
    }
}
