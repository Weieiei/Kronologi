import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/internal/Observable";
import {of} from "rxjs/internal/observable/of";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  employees = ['Matthew Teolis',
    'Someone Else',
    'Another Person'];

  constructor(private http: HttpClient) { }

  public getAllEmployees(): Observable<any[]> {
    return this.http.get<any[]>(['api', 'employees'].join('/'));
  }

  public getAvailableEmployees(date: Date): Observable<any[]> {
    // return this.http.get<any[]>('api') todo ... use date
    return of<any[]>(this.employees);
  }
}
