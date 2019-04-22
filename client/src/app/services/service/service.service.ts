import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { switchMap, shareReplay, map, takeUntil } from 'rxjs/operators';
import { timer } from 'rxjs/observable/timer';
import { Service } from '../../models/service/Service';
import { ServiceCreateDto } from '../../interfaces/service/service-create-dto';
import { ServiceDTO } from '../../interfaces/service/service-dto';

const REFRESH_INTERVAL = 10000;
const CACHE_SIZE = 1;

@Injectable({
    providedIn: 'root'
})
export class ServiceService {

    private cache$: Observable<Array<Service>>;
    private reload$ = new Subject<void>();
    private businessId: number;

    constructor(private http: HttpClient) {
    }

    setBusinessId(businessId: number) {
        this.businessId = businessId;
    }

    get allServices() {
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

    public getServices(businessId: number): Observable<ServiceDTO[]> {
        return this.http.get<ServiceDTO[]>(['api', 'business', businessId, 'services'].join('/'));
    }

    public getPlainServices(businessId: number): Observable<Service[]> {
        return this.http.get<Service[]>(['api', 'business', businessId, 'services'].join('/'));
    }

    requestAllEmployees() {
        return this.getPlainServices(this.businessId).pipe(
            map(data => {
                return data.map(a => {
                    return new Service(
                        a.id, a.name, a.duration
                    );
                });
            })
        );
    }

    forceReload() {
        this.reload$.next();
        this.cache$ = null;
    }

    public createService(businessId: number, service: ServiceCreateDto): Observable<any> {
        return this.http.post<Service>(['api', 'business', businessId, 'admin', 'service'].join('/'), service);
    }
    public registerService(businessId: number, service: ServiceCreateDto): Observable<any> {
        return this.http.post<Service>(['api', 'business', 'services', businessId, 'service'].join('/'), service);
    }

    // TODO: need to add the businessID instead of s=using '1'
    public addServiceToUser(employeedId: number, serviceId: number, businessId:number): Observable<any> {
        return this.http.post<any>(['api',  'business', businessId, 'admin' , 'service', employeedId, serviceId].join('/'), '');
    }

    public updateServicePicture(serviceFile: File, serviceId: number): Observable<any> {
        let formData = new FormData();
        formData.append('file', serviceFile);
        return this.http.post(['api', 'business', 'services', serviceId, 'profile'].join('/'), formData);
    }

    public getServiceProfile(businessId: number, serviceId: number): Observable<any> {
        return this.http.get<any>(['api', 'business', businessId, 'services', serviceId, 'profile'].join('/'));
    }
}
