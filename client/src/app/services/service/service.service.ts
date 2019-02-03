import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Service } from '../../models/service/Service';
import { ServiceDTO } from "../../interfaces/service/service-dto";

@Injectable({
    providedIn: 'root'
})
export class ServiceService {

    constructor(private http: HttpClient) {
    }

    public getServices(): Observable<Service[]> {
        return this.http.get<Service[]>(['api', 'services'].join('/'));
    }

    public createService(service: ServiceDTO): Observable<any> {
        return this.http.post<Service>(['api', 'admin', 'service'].join('/'), service);
    }
}
