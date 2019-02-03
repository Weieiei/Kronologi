import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Service } from '../../models/service/Service';
import { ServiceCreateDto } from "../../interfaces/service/service-create-d-t-o";

@Injectable({
    providedIn: 'root'
})
export class ServiceService {

    constructor(private http: HttpClient) {
    }

    public getServices(): Observable<Service[]> {
        return this.http.get<Service[]>(['api', 'services'].join('/'));
    }

    public createService(service: ServiceCreateDto): Observable<any> {
        return this.http.post<Service>(['api', 'admin', 'service'].join('/'), service);
    }
}
