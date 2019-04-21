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

    public getServices(businessId: number): Observable<ServiceDTO[]> {
        return this.http.get<ServiceDTO[]>(['api', 'business', 'services', businessId].join('/'));
    }

    public getPlainServices(): Observable<Service[]> {
        return this.http.get<Service[]>(['api', 'business', 'services', '1'].join('/'));
    }

    public createService(businessId: number, service: ServiceCreateDto): Observable<any> {
        return this.http.post<Service>(['api', 'business', businessId.toString(), 'admin', 'service'].join('/'), service);
    }
    public registerService(businessId: number, service: ServiceCreateDto): Observable<any> {
        return this.http.post<Service>(['api', 'business', 'services', businessId.toString(), 'service'].join('/'), service);
    }

    // TODO: need to add the businessID instead of s=using '1'
    public addServiceToUser(employeedId: number, serviceId: number): Observable<any> {
        return this.http.post<any>(['api', 'business', 'admin', '1', 'service', employeedId, serviceId].join('/'), '');
    }

    public updateServicePicture(serviceFile: File, serviceId: number): Observable<any> {
        let formData = new FormData();
        formData.append('file', serviceFile);
        return this.http.post(['api', 'business', 'services', serviceId.toString(), 'profile'].join('/'), formData);
    }

     public getServiceProfile(serviceId: number): Observable<any> {
        return this.http.get<any>(['api', 'business', 'services' , serviceId.toString(), 'profile'].join('/'));
    }
}
