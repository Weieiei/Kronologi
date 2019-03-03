import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BusinessDTO } from '../../interfaces/business/business-dto';
import { Business } from '../../interfaces/business/business';
import { ServiceDTO } from '../../interfaces/service/service-dto';
import { BusinessRegisterDTO } from '../../interfaces/business/business-register-dto';

@Injectable({
    providedIn: 'root'
})
export class BusinessService {

    constructor(private http: HttpClient) {
    }

    public createBusiness(business: BusinessRegisterDTO): Observable<any> {
        return this.http.post<Business>(['api', 'admin', 'service'].join('/'), business);
    }

}
