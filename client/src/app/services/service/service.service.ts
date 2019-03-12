import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Service } from '../../models/service/Service';
import { ServiceCreateDto } from '../../interfaces/service/service-create-dto';
import { ServiceDTO } from '../../interfaces/service/service-dto';

@Injectable({
    providedIn: 'root'
})
export class ServiceService {

    constructor(private http: HttpClient) {
    }

    public getServices(): Observable<ServiceDTO[]> {
        return this.http.get<ServiceDTO[]>(['api', 'business','services', 1].join('/'));
    }

    public getPlainServices(): Observable<Service[]> {
        return this.http.get<Service[]>(['api', 'business','services','1'].join('/'));
    }

    public createService(service: ServiceCreateDto): Observable<any> {
        return this.http.post<Service>(['api','business', 'admin','1', 'service'].join('/'), service);
    }

    public addServiceToUser(employeedId: number, serviceId: number):Observable<any> {
        return this.http.post<any>(['api', 'business', 'admin','1', 'service', employeedId, serviceId].join('/'), "");
    }
}
