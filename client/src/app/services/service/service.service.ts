import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Service } from '../../models/service/Service';

@Injectable({
    providedIn: 'root'
})
export class ServiceService {

    constructor(private http: HttpClient) {
    }

    public getServices(): Observable<Service[]> {
        return this.http.get<Service[]>(['api', 'services'].join('/'));
    }

}
