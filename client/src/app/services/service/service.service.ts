import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ServiceDTO } from '../../interfaces/service/service-dto';

@Injectable({
    providedIn: 'root'
})
export class ServiceService {

    constructor(private http: HttpClient) {
    }

    public getServices(): Observable<ServiceDTO[]> {
        return this.http.get<ServiceDTO[]>(['api', 'services'].join('/'));
    }

}
