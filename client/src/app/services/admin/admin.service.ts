import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject, timer } from 'rxjs';
import { AdminEmployeeDTO } from '../../interfaces/employee/admin-employee-dto';
import { AdminEmployeeShiftDTO } from '../../interfaces/shift/admin-employee-shift-dto';
import { NewShiftDTO } from '../../interfaces/shift/new-shift-dto';
import { shareReplay, switchMap, takeUntil } from 'rxjs/operators';

const REFRESH_INTERVAL = 10000;
const CACHE_SIZE = 1;

@Injectable({
    providedIn: 'root'
})
export class AdminService {

    private cache$: Observable<Array<AdminEmployeeDTO>>;
    private reload$ = new Subject<void>();
    private businessId: number;

    constructor(private http: HttpClient) {
    }

    setBusinessId(businessId: number) {
        this.businessId = businessId;
    }

    get allEmployees() {
        if (!this.cache$) {
            const timer$ = timer(0, REFRESH_INTERVAL);

            this.cache$ = timer$.pipe(
                switchMap(() => this.requestAllEmployees()),
                takeUntil(this.reload$),
                shareReplay(CACHE_SIZE)
            );
        }

        return this.cache$;
    }
    requestAllEmployees() {
        return this.getAllEmployees(this.businessId);
    }

    forceReload() {
        this.reload$.next();
        this.cache$ = null;
    }

    public getAllEmployees(businessId: number): Observable<AdminEmployeeDTO[]> {
        return this.http.get<AdminEmployeeDTO[]>(['api', 'business', businessId, 'admin', 'employees'].join('/'));
    }

    getEmployee(businessId: number, employeeId: number): Observable<AdminEmployeeDTO> {
        return this.http.get<AdminEmployeeDTO>(['api', 'business', businessId, 'admin', 'employee', employeeId].join('/'));
    }

    getEmployeeShifts(businessId: number, employeeId: number): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.get<AdminEmployeeShiftDTO[]>(['api', 'business', businessId, 'admin', 'employee', employeeId, 'shift'].join('/'));
    }

    addShift(businessId: number, employeeId: number, payload: NewShiftDTO): Observable<AdminEmployeeShiftDTO> {
        return this.http.post<AdminEmployeeShiftDTO>(['api', 'business', businessId, 'admin', 'employee', employeeId,
            'shift'].join('/'), payload);
    }

     addShiftList(businessId: number, employeeId: number, payload: Array<NewShiftDTO>): Observable<AdminEmployeeShiftDTO[]> {
        return this.http.post<AdminEmployeeShiftDTO[]>(['api', 'business', businessId, 'admin', 'employee', employeeId,
            'shift-list'].join('/'), payload);
    }

    deleteShift(businessId: number, shiftId: number): Observable<any> {
        return this.http.delete<any>(['api', 'business', businessId, 'admin', 'employee', 'shift', shiftId].join('/'));
    }
}
